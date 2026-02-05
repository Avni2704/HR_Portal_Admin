package steps;

import ScenarioContext.ScenarioContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import qa.util.ExternalFunction;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static drivers.DriverInstance.driver;

public class AdminCompanyBenefitsSteps {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

    @When("User click on Company Benefit")
    public void userClickOnCompanyBenefit() {

        // Wait for loader overlay to disappear (using your helper)
        ExternalFunction.waitForLoaderToDisappear(driver);

        // Wait for the Company Benefit element to be ready
        WebElement companyBenefitSection = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//p[normalize-space(text())='Company Benefits']")
                )
        );

        wait.until(ExpectedConditions.visibilityOf(companyBenefitSection));

        // Attempt click (with JS fallback)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(companyBenefitSection));
            companyBenefitSection.click();
            System.out.println("Clicked on 'Company Benefits' normally.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, using JavaScript click instead...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", companyBenefitSection);
            js.executeScript("arguments[0].click();", companyBenefitSection);
            System.out.println("Clicked on 'Company Benefits' via JavaScript.");
        }

        // Wait again after navigation
        try {
            ExternalFunction.waitForLoaderToDisappear(driver);
            System.out.println("Page finished loading after navigation.");
        } catch (TimeoutException e) {
            System.out.println("Loader did not disappear after navigation, proceeding...");
        }
    }

    @And("User should see Job Position header")
    public void userShouldSeeJobPositionHeader() {
        try {
            // Wait until the header "Job Position" becomes visible
            WebElement header = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h1[normalize-space(text())='Job Position']")
                    )
            );

            System.out.println("Header found: " + header.getText());
            Assert.assertTrue(header.isDisplayed(), "Job Position header is not displayed");

        } catch (TimeoutException e) {
            System.out.println("Header 'Job Position' not found within timeout!");
            Assert.fail("Header 'Job Position' not found!");
        }

    }

    @When("User click on Add New Position")
    public void userClickOnAddNewPosition() {
        WebElement AddNewPostBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]/button")));

        // Wait until it's clickable
        wait.until(ExpectedConditions.elementToBeClickable(AddNewPostBtn ));

        // Try clicking normally first
        AddNewPostBtn .click();
        System.out.println("Add New Position Button successfully clicked");
    }

    @And("User enter Position Name {string}")
    public void userEnterPositionName(String positionName) {
        if (positionName.contains("[MathRand]")) {
            String randomValue = String.valueOf(System.currentTimeMillis());
            positionName = positionName.replace("[MathRand]", randomValue);
        }

        WebElement positionNameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.name("name")
                )
        );

        positionNameInput.clear();
        positionNameInput.sendKeys(positionName);

        // Store generated value
        ScenarioContext.setContext("POSITION_NAME", positionName);
    }

    @And("User enter Description {string}")
    public void userEnterDescription(String jobDesc) {

        By descriptionTextarea = By.xpath("//textarea[@placeholder='Please enter a description of the position']");

        WebElement description = wait.until(
                ExpectedConditions.visibilityOfElementLocated(descriptionTextarea)
        );

        description.sendKeys(jobDesc);
    }

    @Then("User click on Create button")
    public void userClickOnCreateButton() {
        WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Create']]")));

        createButton.click();
    }

    @And("User should see created Job Position on the page")
    public void userShouldSeeCreatedJobPositionOnThePage() {
        /*ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);
        String createdPosition = (String) ScenarioContext.getContext("POSITION_NAME");

        By cellLocator = By.xpath("//td[text()='" + createdPosition + "']");

        WebElement createdJob = ExternalFunction.findElementInPaginatedTable(driver, wait, cellLocator, createdPosition, 20);

        Assert.assertTrue(createdJob.isDisplayed(),
                "Created Job Position is not displayed on the page");*/
        ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);
        String createdPosition = (String) ScenarioContext.getContext("POSITION_NAME");

        By cellLocator = By.xpath("//td[text()='" + createdPosition + "']");
        WebElement createdJob = ExternalFunction.findElementInPaginatedTable(driver, wait, cellLocator, createdPosition, 20);

        // Validate visibility
        Assert.assertTrue(createdJob.isDisplayed(), "Created Job Position is not displayed on the page");

        // Validate row data
        WebElement row = createdJob.findElement(By.xpath("./ancestor::tr"));
        String actualJobName = row.findElement(By.xpath("./td[2]")).getText();
        String trimmedCreatedJob = createdJob.getText().trim();
        Assert.assertEquals(actualJobName, trimmedCreatedJob);
        System.out.println("Expected result : " + createdPosition + "Actual result : " + trimmedCreatedJob);
    }

    @Then("User click on Cancel button")
    public void userClickOnCancelButton() {
        // Write code here that turns the phrase above into concrete actions
        WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Cancel']]")));

        createButton.click();
    }

    @And("Form modal should close and no Job Position should be created")
    public void formModalShouldCloseAndNoJobPositionShouldBeCreated() {
        ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);

        String createdPosition = (String) ScenarioContext.getContext("POSITION_NAME");

        By cellLocator = By.xpath("//td[text()='" + createdPosition + "']");

        boolean jobExists = false;

        try {
            // Try to find the job in paginated table
            WebElement createdJob = ExternalFunction.findElementInPaginatedTable(
                    driver,
                    wait,
                    cellLocator,
                    createdPosition,
                    20
            );

            // If found and displayed → job EXISTS (which is a failure case)
            if (createdJob != null && createdJob.isDisplayed()) {
                jobExists = true;
            }

        } catch (Exception e) {
            // Expected behaviour:
            // Job position NOT found in table across pagination
            jobExists = false;
        }

        Assert.assertFalse(
                jobExists,
                "Job Position was created, but it should NOT be created"
        );

        System.out.println("Validation passed: Job Position '" + createdPosition + "' was NOT created.");
    }

    @And("An existing Job Position is available for editing")
    public void anExistingJobPositionIsAvailableForEditing() {

        ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);

        List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
        Assert.assertFalse(rows.isEmpty(), "No Job Position available for editing");

        WebElement firstRow = rows.get(0);

        String existingPosition =
                firstRow.findElement(By.xpath("./td[2]")).getText().trim();

        Assert.assertNotNull(existingPosition, "Existing Job Position name is null");

        ScenarioContext.setContext("OLD_POSITION_NAME", existingPosition);

        System.out.println("Editing Job Position: " + existingPosition);
    }

    @When("User clicks on the Edit button for the Job Position")
    public void userClicksOnTheEditButtonForTheJobPosition() {
        String positionName =
                (String) ScenarioContext.getContext("OLD_POSITION_NAME");

        Assert.assertNotNull(
                positionName,
                "OLD_POSITION_NAME is null. Ensure 'An existing Job Position is available for editing' step ran successfully."
        );

        By editButtonLocator = By.xpath(
                "//tr[td[normalize-space()='" + positionName + "']]//button[contains(@class,'table__action')]"
        );

        WebElement editButton = wait.until(
                ExpectedConditions.elementToBeClickable(editButtonLocator)
        );

        editButton.click();
    }

    @And("User clears the Position Name field")
    public void userClearsThePositionNameField() {
        WebElement positionNameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("name"))
        );

        positionNameInput.clear();
    }

    @And("User enters a new valid Position Name {string}")
    public void userEntersANewValidPositionName(String newPost) {
        String newPositionName = newPost + System.currentTimeMillis();

        WebElement positionNameInput = driver.findElement(By.name("name"));
        positionNameInput.sendKeys(newPositionName);

        ScenarioContext.setContext("NEW_POSITION_NAME", newPositionName);
    }

    @And("User clears the Description field")
    public void userClearsTheDescriptionField() {
        By descriptionTextarea = By.xpath("//textarea[@placeholder='Please enter a description of the position']");

        WebElement descriptionInput = driver.findElement(descriptionTextarea);
        descriptionInput.clear();
    }

    @And("User enters a new valid Description {string}")
    public void userEntersANewValidDescription(String jobDesc) {
        String updateJobDesc = jobDesc + System.currentTimeMillis();
        By descriptionTextarea = By.xpath("//textarea[@placeholder='Please enter a description of the position']");
        WebElement descriptionInput = driver.findElement(descriptionTextarea);
        descriptionInput.sendKeys(updateJobDesc);

        ScenarioContext.setContext("NEW_DESCRIPTION", updateJobDesc);
    }

    @And("User clicks the Save button")
    public void userClicksTheSaveButton() {
        WebElement saveButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Save']"))
        );

        saveButton.click();
    }

    @Then("The edit Job Position modal should close")
    public void theEditJobPositionModalShouldClose() {
        By modalLocator = By.className("position-modal");

        boolean isClosed = wait.until(
                ExpectedConditions.invisibilityOfElementLocated(modalLocator)
        );

        Assert.assertTrue(isClosed, "Edit Job Position modal did not close");
    }

    @And("User should see a success message for Job Position update")
    public void userShouldSeeASuccessMessageForJobPositionUpdate() {
        By successToastLocator = By.xpath(
                "//div[@id='app-alert' and contains(@class,'app-alert--active')]//p[@class='alert__text' and normalize-space()='Position has been updated successfully']"
        );

        WebElement successToast = wait.until(
                ExpectedConditions.visibilityOfElementLocated(successToastLocator)
        );

        Assert.assertTrue(
                successToast.isDisplayed(),
                "Success toast message for Job Position update is not displayed"
        );

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("app-alert")));

    }

    @And("The Job Position should be updated with the new details in the listing")
    public void theJobPositionShouldBeUpdatedWithTheNewDetailsInTheListing() {
        ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);

        String newPositionName =
                (String) ScenarioContext.getContext("NEW_POSITION_NAME");

        By cellLocator = By.xpath("//td[normalize-space()='" + newPositionName + "']");

        WebElement updatedJob = ExternalFunction.findElementInPaginatedTable(
                driver,
                wait,
                cellLocator,
                newPositionName,
                20
        );

        Assert.assertTrue(
                updatedJob.isDisplayed(),
                "Updated Job Position is not displayed in the listing"
        );
    }

    @Then("User should see a validation error message for Position Name")
    public void userShouldSeeAValidationErrorMessageForPositionName() {
        By positionNameErrorLocator = By.xpath(
                "//p[contains(@class,'MuiFormHelperText-root') " +
                        "and contains(@class,'Mui-error') " +
                        "and normalize-space()='This field is required.']"
        );

        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(positionNameErrorLocator)
        );

        Assert.assertTrue(
                errorMessage.isDisplayed(),
                "Expected validation error message is not displayed"
                );
    }

    @And("The Job Position should not be updated")
    public void theJobPositionShouldNotBeUpdated() {
        String oldPositionName =
                (String) ScenarioContext.getContext("OLD_POSITION_NAME");

        // Search for the original position name in the table
        By positionRowLocator = By.xpath(
                "//tr[td[normalize-space()='" + oldPositionName + "']]"
        );

        List<WebElement> rows = driver.findElements(positionRowLocator);

        Assert.assertFalse(
                rows.isEmpty(),
                "Job Position was updated unexpectedly"
                );
    }

    @And("User enters an existed Position Name {string}")
    public void userEntersAnExistedPositionName(String existPost) {

        WebElement positionNameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("name"))
        );

        positionNameInput.clear();
        positionNameInput.sendKeys(existPost);

        System.out.println("Entered existing Position Name: " + existPost);
    }

    @Then("User should see a message indicating duplicate entry")
    public void userShouldSeeAMessageIndicatingDuplicateEntry() {

        By duplicateToastLocator = By.xpath(
                "//div[contains(@class,'alert')]//p[@class='alert__text' and normalize-space()='Duplicate Entry Found.']"
        );

        WebElement duplicateToast = wait.until(
                ExpectedConditions.visibilityOfElementLocated(duplicateToastLocator)
        );

        Assert.assertTrue(
                duplicateToast.isDisplayed(),
                "Duplicate entry toast message is not displayed"
        );

        System.out.println("Duplicate entry message displayed: " + duplicateToast.getText());
    }

    @And("User click on Benefit Package")
    public void userClickOnBenefitPackage() {
        // Wait for loader overlay to disappear (using your helper)
        ExternalFunction.waitForLoaderToDisappear(driver);

        // Wait for the Company Benefit element to be ready
        WebElement companyBenefitSection = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//p[normalize-space(text())='Benefit Package']")
                )
        );

        wait.until(ExpectedConditions.visibilityOf(companyBenefitSection));

        // Attempt click (with JS fallback)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(companyBenefitSection));
            companyBenefitSection.click();
            System.out.println("Clicked on 'Benefit Package' normally.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, using JavaScript click instead...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", companyBenefitSection);
            js.executeScript("arguments[0].click();", companyBenefitSection);
            System.out.println("Clicked on 'Benefits Package' via JavaScript.");
        }

        // Wait again after navigation
        try {
            ExternalFunction.waitForLoaderToDisappear(driver);
            System.out.println("Page finished loading after navigation.");
        } catch (TimeoutException e) {
            System.out.println("Loader did not disappear after navigation, proceeding...");
        }
    }

    @And("User should see Benefit Package header")
    public void userShouldSeeBenefitPackageHeader() {
        try {
            // Wait until the header "Job Position" becomes visible
            WebElement header = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h1[normalize-space(text())='Benefit Package']")
                    )
            );

            System.out.println("Header found: " + header.getText());
            Assert.assertTrue(header.isDisplayed(), "Benefit Package header is not displayed");

        } catch (TimeoutException e) {
            System.out.println("Header 'Benefit Package' not found within timeout!");
            Assert.fail("Header 'Benefit Package' not found!");
        }
    }

    @When("User click on Add Benefit button")
    public void userClickOnAddBenefitButton() {
        WebElement AddBenefitBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]/button")));

        // Wait until it's clickable
        wait.until(ExpectedConditions.elementToBeClickable(AddBenefitBtn));

        // Try clicking normally first
        AddBenefitBtn.click();
        System.out.println("Add New Position Button successfully clicked");
    }

    @And("User should see New Package page")
    public void userShouldSeeNewPackagePage() {
        try {
            // Wait until the header "Job Position" becomes visible
            WebElement header = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h1[normalize-space(text())='New Package']")
                    )
            );

            System.out.println("Header found: " + header.getText());
            Assert.assertTrue(header.isDisplayed(), "Job Position header is not displayed");

        } catch (TimeoutException e) {
            System.out.println("Header 'New Package' not found within timeout!");
            Assert.fail("Header 'New Package' not found!");
        }
    }

    @And("User input {string} into respecting fields {string}")
    public void userInputDataIntoRespectingFieldsFieldsName(String data, String fieldsName) {

        // ===== Mapping for Leave fields =====
        Map<String, String> leaveFieldMap = new HashMap<>();
        leaveFieldMap.put("Annual Leave", "leaveBenefits.0.numberOfDays");
        leaveFieldMap.put("Sick/Medical", "leaveBenefits.1.numberOfDays");
        leaveFieldMap.put("Hospitalization", "leaveBenefits.2.numberOfDays");
        leaveFieldMap.put("Maternity", "leaveBenefits.3.numberOfDays");
        leaveFieldMap.put("Paternity", "leaveBenefits.4.numberOfDays");
        leaveFieldMap.put("Carry Forward Leave", "extraLeaveBenefits.0.numberOfDays");
        leaveFieldMap.put("Marriage Leave", "extraLeaveBenefits.0.numberOfDays");
        leaveFieldMap.put("Emergency Leave","extraLeaveBenefits.0.numberOfDays");
        leaveFieldMap.put("Unpaid Leave","extraLeaveBenefits.0.numberOfDays");
        leaveFieldMap.put("Compassionate Leave","extraLeaveBenefits.0.numberOfDays");
        leaveFieldMap.put("Replacement Leave","extraLeaveBenefits.0.numberOfDays");

        Map<String, String> claimFieldMap = new HashMap<>();
        claimFieldMap.put("Medical Claim", "claimBenefits.0.amount");
        claimFieldMap.put("Optical/ Dental", "claimBenefits.1.amount");
        claimFieldMap.put("Others", "claimBenefits.2.amount");
        claimFieldMap.put("Transport Claim", "extraClaimBenefits.0.amount");
        claimFieldMap.put("Office Supplies Claim", "extraClaimBenefits.0.amount");

        // ===== Split data and fields =====
        String[] values = data.split("\\s*,\\s*");
        String[] fields = fieldsName.split("\\s*,\\s*");

        Assert.assertEquals(values.length, fields.length, "Data count does not match fields count");

        String[] finalValues = new String[values.length];

        // ===== Loop through each field =====
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i].trim();
            String value = values[i].trim();
            WebElement inputField;
            String finalValue = value;

            if (claimFieldMap.containsKey(field)) {
                inputField = wait.until(ExpectedConditions.elementToBeClickable(
                        By.name(claimFieldMap.get(field))
                ));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", inputField);
                inputField.clear();
                inputField.sendKeys(finalValue);
                finalValues[i] = finalValue;
                continue; // skip the rest of the switch/default
            }

            switch (field) {
                case "name":
                    finalValue = value + "_" + System.currentTimeMillis();
                    inputField = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//input[@placeholder='Package Name' or @name='name']")
                    ));
                    ScenarioContext.setContext("PACKAGE_NAME", finalValue);
                    break;

                case "description":
                    inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//textarea[@placeholder='Please describe this benefit package']")
                    ));
                    break;

                default:
                    // ===== Handle Leave fields =====
                    if (leaveFieldMap.containsKey(field)) {
                        inputField = wait.until(ExpectedConditions.elementToBeClickable(
                                By.name(leaveFieldMap.get(field))
                        ));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", inputField);
                        inputField.clear();
                        inputField.sendKeys(finalValue);

                        // Only for Carry Forward Leave: handle expiry
                        if (field.equalsIgnoreCase("Carry Forward Leave")) {
                            WebElement expiryDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                                    By.id("mui-component-select-extraLeaveBenefits.0.expiryMonths")
                            ));
                            expiryDropdown.click();

                            WebElement expiryOption = wait.until(ExpectedConditions.elementToBeClickable(
                                    By.xpath("//li[normalize-space()='12 Months']")
                            ));
                            expiryOption.click();
                        }

                        finalValues[i] = finalValue;
                        continue; // skip below sendKeys
                    }

                    // ===== Other fields (claims, etc) =====
                    inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//label[contains(normalize-space(),'" + field + "')]/ancestor::div[contains(@class,'benefit__input')]//input[@type='text']")
                    ));
                    break;
            }

            inputField.clear();
            inputField.sendKeys(finalValue);
            finalValues[i] = finalValue;
            System.out.println("Entered value '" + finalValue + "' into field '" + field + "'");
        }

        ScenarioContext.setContext("PACKAGE_FIELDS", fields);
        ScenarioContext.setContext("PACKAGE_VALUES", finalValues);
    }

    @Then("User click on update button")
    public void userClickOnUpdateButton() {
        WebElement updateButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Update']"))
        );

        updateButton.click();
        System.out.println("Clicked on Update button");
    }

    @And("User should see New Package created on the Benefit Package page with correct data")
    public void userShouldSeeNewPackageCreatedOnTheBenefitPackagePageWithCorrectData() {

        ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);

        String createdPackageName = (String) ScenarioContext.getContext("PACKAGE_NAME");

        By cellLocator = By.xpath("//td[normalize-space()='" + createdPackageName + "']");

        WebElement createdPackageCell =
                ExternalFunction.findElementInPaginatedTable(
                        driver,
                        wait,
                        cellLocator,
                        createdPackageName,
                        20
                );

        Assert.assertTrue(
                createdPackageCell.isDisplayed(),
                "Created Benefit Package is not displayed in the table"
        );

        System.out.println(
                "Benefit Package successfully found: " + createdPackageName
        );
    }

    @And("User click on Add New Leave button")
    public void userClickOnAddNewLeaveButton() {
        WebElement updateButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Add New Leave']"))
        );

        updateButton.click();
        System.out.println("Clicked on Add New Leave button");
    }

    @And("User select Leave {string}")
    public void userSelectLeaveTypeAndEnterDaysNumber(String leaveType) {
        // 1. Click the Leave Type dropdown
        WebElement leaveDropdown = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath(
                                "//label[contains(text(),'Leave Type')]" +
                                        "/following::div[contains(@class,'MuiSelect-select')][1]"
                        )
                )
        );
        leaveDropdown.click();

        // 2. Select the leave type from dropdown list
        WebElement leaveOption = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//li[normalize-space()='" + leaveType + "']")
                )
        );
        leaveOption.click();

        System.out.println("Selected Leave Type: " + leaveType);
    }

    @And("User click on Add New Claim button")
    public void userClickOnAddNewClaimButton() {
        WebElement updateButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Add New Claim']"))
        );

        updateButton.click();
        System.out.println("Clicked on Add New Claim button");
    }

    @And("User select Claim {string}")
    public void userSelectClaimType(String claimType) {
        // 1. Click the Claim Type dropdown
        WebElement claimTypeDropdown = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath(
                                "//p[text()='New Claim']" +
                                        "/ancestor::div[contains(@class,'benefit__input')]" +
                                        "//div[contains(@class,'MuiSelect-select')]"
                        )
                )
        );
        claimTypeDropdown.click();

        // 2. Select the claim type from dropdown list
        WebElement claimOption = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//li[normalize-space()='" + claimType + "']")
                )
        );
        claimOption.click();

        System.out.println("Selected Claim Type: " + claimType);

    }


    @And("User should not see New Package created on the Benefit Package page")
    public void userShouldNotSeeNewPackageCreatedOnTheBenefitPackagePage() {
        ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);

        String createdPackageName = (String) ScenarioContext.getContext("PACKAGE_NAME");
        By cellLocator = By.xpath("//td[normalize-space()='" + createdPackageName + "']");

        boolean isFound = true;

        try {
            WebElement createdPackageCell = ExternalFunction.findElementInPaginatedTable(
                    driver,
                    wait,
                    cellLocator,
                    createdPackageName,
                    20
            );

            // If element is found, check if it's displayed
            isFound = createdPackageCell.isDisplayed();
        } catch (RuntimeException e) {
            // Element not found in table → this is the expected outcome
            isFound = false;
        }

        Assert.assertFalse(
                isFound,
                "Benefit Package should NOT be displayed in the table"
        );

        System.out.println(
                "Benefit Package not found as expected: " + createdPackageName
        );
    }


    @And("an existing Benefit Package is available for editing")
    public void anExistingBenefitPackageIsAvailableForEditing() {
        ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);

        List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
        Assert.assertFalse(rows.isEmpty(), "No Benefit Package available for editing");

        WebElement firstRow = rows.get(9);

        String existingPackage =
                firstRow.findElement(By.xpath("./td[1]")).getText().trim();

        Assert.assertNotNull(existingPackage, "Existing Job Position name is null");

        ScenarioContext.setContext("OLD_PACKAGE_NAME", existingPackage);

        System.out.println("Editing Benefit Package: " + existingPackage);
    }

    @When("the user clicks the Edit button for the Benefit Package")
    public void theUserClicksTheEditButtonForTheBenefitPackage() {
        String positionName =
                (String) ScenarioContext.getContext("OLD_PACKAGE_NAME");

        Assert.assertNotNull(
                positionName,
                "OLD_PACKAGE_NAME is null. Ensure 'An existing Benefit Package is available for editing' step ran successfully."
        );

        By editButtonLocator = By.xpath(
                "//tr[td[normalize-space()='" + positionName + "']]//button[contains(@class,'table__action')]"
        );

        WebElement editButton = wait.until(
                ExpectedConditions.elementToBeClickable(editButtonLocator)
        );

        editButton.click();
    }

    @And("the user clears the Package Name field")
    public void theUserClearsThePackageNameField() {
        WebElement positionNameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("name"))
        );

        positionNameInput.clear();
    }

    @And("the user enters a new valid Package Name {string}")
    public void theUserEntersANewValidPackageNameNewPackage(String newPackage) {
        String newPackageName = newPackage + System.currentTimeMillis();

        WebElement positionNameInput = driver.findElement(By.name("name"));
        positionNameInput.sendKeys(newPackageName);

        ScenarioContext.setContext("NEW_PACKAGE_NAME", newPackageName);
    }

    @And("the user clears the Description field")
    public void theUserClearsTheDescriptionField() {
        By descriptionTextarea = By.xpath("//textarea[@placeholder='Please describe this benefit package']");

        WebElement descriptionInput = driver.findElement(descriptionTextarea);
        descriptionInput.clear();
    }

    @And("the user enters a new valid Description {string}")
    public void theUserEntersANewValidDescriptionNewDesc(String newDesc) {
        String updatePackDesc = newDesc + System.currentTimeMillis();
        By descriptionTextarea = By.xpath("//textarea[@placeholder='Please describe this benefit package']");
        WebElement descriptionInput = driver.findElement(descriptionTextarea);
        descriptionInput.sendKeys(updatePackDesc);

        ScenarioContext.setContext("NEW_DESCRIPTION", updatePackDesc);
    }

    @And("the user updates the Leave Types {string} with Leave Days {string}")
    public void theUserUpdatesTheLeaveTypesLeaveTypesWithLeaveDaysLeaveDays(String leaveTypes, String leaveDays) {
        String[] types = leaveTypes.split("\\s*,\\s*");
        String[] days  = leaveDays.split("\\s*,\\s*");

        Assert.assertEquals(
                types.length,
                days.length,
                "Leave types count does not match leave days count"
        );

        for (int i = 0; i < types.length; i++) {

            String leaveType = types[i];
            String dayValue  = days[i];

            System.out.println("Updating Extra Leave: " + leaveType + " = " + dayValue);

            // 1️⃣ Click Leave Type dropdown
            By dropdown = By.id("mui-component-select-extraLeaveBenefits." + i + ".type");
            WebElement dropdownEl = wait.until(ExpectedConditions.elementToBeClickable(dropdown));
            dropdownEl.click();

            // 2️⃣ Select leave type from dropdown list
            By option = By.xpath("//li[normalize-space()='" + leaveType + "']");
            wait.until(ExpectedConditions.elementToBeClickable(option)).click();

            // 3️⃣ Update number of days
            By daysInput = By.name("extraLeaveBenefits." + i + ".numberOfDays");
            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(daysInput));
            input.clear();
            input.sendKeys(dayValue);
        }
    }

    @And("the user updates the Claim Types {string} with Claim Amounts {string}")
    public void theUserUpdatesTheClaimTypesClaimTypesWithClaimAmountsClaimAmounts(String claimTypes, String claimAmounts) {
        String[] types   = claimTypes.split("\\s*,\\s*");
        String[] amounts = claimAmounts.split("\\s*,\\s*");

        Assert.assertEquals(
                types.length,
                amounts.length,
                "Claim types count does not match claim amounts count"
        );

        for (int i = 0; i < types.length; i++) {

            String claimType = types[i];
            String amount    = amounts[i];

            System.out.println("Updating Extra Claim: " + claimType + " = " + amount);

            // 1️⃣ Click Claim Type dropdown
            By dropdown = By.id("mui-component-select-extraClaimBenefits." + i + ".type");
            WebElement dropdownEl = wait.until(ExpectedConditions.elementToBeClickable(dropdown));
            dropdownEl.click();

            // 2️⃣ Select claim type
            By option = By.xpath("//li[normalize-space()='" + claimType + "']");
            wait.until(ExpectedConditions.elementToBeClickable(option)).click();

            // 3️⃣ Update claim amount
            By amountInput = By.name("extraClaimBenefits." + i + ".amount");
            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(amountInput));
            input.clear();
            input.sendKeys(amount);
        }
    }

    @Then("the user clicks the Update button")
    public void theUserClicksTheUpdateButton() {
        WebElement saveButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Update']"))
        );

        saveButton.click();
    }

    @And("the user should see a success message for Benefit Package update")
    public void theUserShouldSeeASuccessMessageForBenefitPackageUpdate() {
        By successToastLocator = By.xpath(
                "//div[@id='app-alert' and contains(@class,'app-alert--active')]//p[@class='alert__text' and normalize-space()='Benefit has been updated successfully']"
        );

        WebElement successToast = wait.until(
                ExpectedConditions.visibilityOfElementLocated(successToastLocator)
        );

        Assert.assertTrue(
                successToast.isDisplayed(),
                "Success toast message for Benefit Package update is not displayed"
        );

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("app-alert")));
    }

    @And("the Benefit Package should be updated with the new details in the listing")
    public void theBenefitPackageShouldBeUpdatedWithTheNewDetailsInTheListing() {
        ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);

        String newPackageName =
                (String) ScenarioContext.getContext("NEW_PACKAGE_NAME");

        By cellLocator = By.xpath("//td[normalize-space()='" + newPackageName + "']");

        WebElement updatedPackage = ExternalFunction.findElementInPaginatedTable(
                driver,
                wait,
                cellLocator,
                newPackageName,
                20
        );

        Assert.assertTrue(
                updatedPackage.isDisplayed(),
                "Updated Job Position is not displayed in the listing"
        );
    }
}
