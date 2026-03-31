package steps;

import ScenarioContext.ScenarioContext;
import drivers.DriverInstance;
import hooks.Hooks;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.AssertJUnit;
import qa.util.ExternalFunction;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.*;
import java.util.stream.Collectors;

import static qa.util.ExternalFunction.findElementInPaginatedTable;
import static qa.util.ExternalFunction.getUITableData;

public class AdminUserManagementSteps extends DriverInstance {
    WebDriver driver = Hooks.driver;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
    String generatedEmail;
    String mainWindowHandle;
    String retrievedOTP;

    @And("User click on User Management")
    public void userClickOnUserManagement() {
        // Wait for loader overlay to disappear (using your helper)
        ExternalFunction.waitForLoaderToDisappear(driver);

        // Wait for the Company Benefit element to be ready
        WebElement allowanceManagementSection = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//p[normalize-space(text())='User Management']")
                )
        );

        wait.until(ExpectedConditions.visibilityOf(allowanceManagementSection));
        // Attempt click (with JS fallback)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(allowanceManagementSection));
            allowanceManagementSection.click();
            System.out.println("Clicked on 'User Management' normally.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, using JavaScript click instead...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", allowanceManagementSection);
            js.executeScript("arguments[0].click();", allowanceManagementSection);
            System.out.println("Clicked on 'User Management' via JavaScript.");
        }
        // Wait again after navigation
        try {
            ExternalFunction.waitForLoaderToDisappear(driver);
            System.out.println("Page finished loading after navigation.");
        } catch (TimeoutException e) {
            System.out.println("Loader did not disappear after navigation, proceeding...");
        }
    }

    @And("User can see Employee header")
    public void userCanSeeEmployeeHeader() {
        try {
            WebElement header = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h1[normalize-space(text())='Employee']")
                    )
            );

            System.out.println("Header found: " + header.getText());
            Assert.assertTrue(header.isDisplayed(), "Employee header is not displayed");

        } catch (TimeoutException e) {
            System.out.println("Header 'Employee' not found within timeout!");
            Assert.fail("Header 'Employee' not found!");
        }
        allureScreenshot();
    }

    @And("admin click Invite button")
    public void admin_click_invite_button() {
        WebElement inviteButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Invite']]")));
        inviteButton.click();
        System.out.println("Invite button clicked");
    }

    @And("admin generates a new dummy email")
    public void adminGeneratesDummyEmail() {

        String randomString = UUID.randomUUID().toString().substring(0, 8);
        generatedEmail = randomString + "@mailsac.com";

        System.out.println("Generated Email: " + generatedEmail);
    }

    @And("admin fill personal info section {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}")
    public void admin_fill_personal_info_section(String fullName, String idType, String idInfo, String mobileNo, String dobYear, String dobMonth, String dobDay, String gender, String maritalStatus) {

        // -------- Full Name --------
        if (fullName.contains("[RandomUUID]")) {
            String uuid = java.util.UUID.randomUUID().toString().replaceAll("[^A-Za-z]", "");
            uuid = uuid.substring(0, 3); // take first 8 letters
            fullName = fullName.replace("[RandomUUID]", uuid);
        }

        WebElement fullNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='name']")
        ));
        fullNameInput.clear();
        fullNameInput.sendKeys(fullName);
        System.out.println("Full Name entered: " + fullName);

        // -------- Email Employee --------
        driver.findElement(By.xpath("//input[@name='email']")).sendKeys(generatedEmail);

        ScenarioContext.setContext("EMP_EMAIL", generatedEmail);

        // -------- Identification Type --------
        WebElement idTypeDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-idType")
        ));
        idTypeDropdown.click();

        WebElement idTypeOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" + idType + "']")
        ));
        idTypeOption.click();
        System.out.println("ID Type selected: " + idType);

        // -------- Identification Info (NRIC / Passport) --------
        if (idInfo.contains("[MathRand]")) {
            // Generate 8-digit random number (adjust length as needed)
            String randomValue = String.valueOf((long)(Math.random() * 1_0000_0000L));
            idInfo = idInfo.replace("[MathRand]", randomValue);
        }

        WebElement idInfoInput;
        if (idType.equalsIgnoreCase("NRIC")) {
            idInfoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@name='nric']")
            ));
        } else {
            idInfoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@name='passport']")
            ));
        }

        idInfoInput.clear();
        idInfoInput.sendKeys(idInfo);
        System.out.println("ID Info entered: " + idInfo);

        // -------- Mobile No --------
        WebElement mobileNoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='mobileNo']")
        ));
        mobileNoInput.clear();
        mobileNoInput.sendKeys(mobileNo);
        System.out.println("Mobile No entered: " + mobileNo);

        // -------- Date of Birth (ONLY for Passport) --------
        if (idType.equalsIgnoreCase("Passport")) {

            // Open DOB calendar
            WebElement dobIcon = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("/html/body/div[2]/div[3]/div/form/div[8]/div/div")
            ));
            dobIcon.click();
            System.out.println("DOB calendar opened");

            // Click Year text to open year list
            WebElement yearHeader = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//p[@class='header__year']")
            ));
            yearHeader.click();

            // Select Year
            WebElement yearOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//ul[@id='years']//span[normalize-space()='" + dobYear + "']")
            ));
            yearOption.click();
            System.out.println("DOB Year selected: " + dobYear);

            // Click Month text to open month list
            WebElement monthHeader = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//p[@class='header__month']")
            ));
            monthHeader.click();

            // Select Month
            WebElement monthOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//ul[@id='months']//span[normalize-space()='" + dobMonth + "']")
            ));
            monthOption.click();
            System.out.println("DOB Month selected: " + dobMonth);

            // Select Day
            WebElement dayOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//ul[contains(@class,'dates')]//li"
                            + "[not(contains(@class,'dates__item--disabled'))]"
                            + "//div[normalize-space()='" + dobDay + "']")
            ));
            dayOption.click();
            System.out.println("DOB Day selected: " + dobDay);

            // Click OK
            WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//p[normalize-space()='Ok'] and not(@disabled)]")
            ));
            okButton.click();

            System.out.println("DOB confirmed: "
                    + dobDay + " " + dobMonth + " " + dobYear);
        }

        // -------- Gender (Button) --------
        WebElement genderButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='app-button-input__container']"
                        + "//button[normalize-space()='" + gender + "']")
        ));
        genderButton.click();
        System.out.println("Gender selected: " + gender);

        // -------- Marital Status --------
        WebElement maritalStatusDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-maritalStatus")
        ));
        maritalStatusDropdown.click();

        WebElement maritalStatusOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" + maritalStatus + "']")
        ));
        maritalStatusOption.click();
        System.out.println("Marital Status selected: " + maritalStatus);

        allureScreenshot();
    }

    @And("admin fill employee info section {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}")
    public void admin_fill_employee_info_section(String joinDateYear, String joinDateMonth, String joinDateDay, String probEndDateYear, String probEndDateMonth, String probEndDateDay, String position, String level, String contractType, String reportingManager) {

        // -------- Join Date --------
        // Open Join Date calendar
        WebElement joinDateIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[2]/div[3]/div/form/div[12]/div/div")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", joinDateIcon);
        joinDateIcon.click();
        System.out.println("Join Date calendar opened");

        // Click Year text to open year list
        WebElement yearHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[@class='header__year']")
        ));
        yearHeader.click();

        // Select Year
        WebElement yearOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[@id='years']//span[normalize-space()='" + joinDateYear + "']")
        ));
        yearOption.click();
        System.out.println("Join Date Year selected: " + joinDateYear);

        // Click Month text to open month list
        WebElement monthHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[@class='header__month']")
        ));
        monthHeader.click();

        // Select Month
        WebElement monthOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[@id='months']//span[normalize-space()='" + joinDateMonth + "']")
        ));
        monthOption.click();
        System.out.println("Join Date Month selected: " + joinDateMonth);

        // Select Day
        WebElement dayOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[contains(@class,'dates')]//li"
                        + "[not(contains(@class,'dates__item--disabled'))]"
                        + "//div[normalize-space()='" + joinDateDay + "']")
        ));
        dayOption.click();
        System.out.println("Join Date Day selected: " + joinDateDay);

        // Click OK
        WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//p[normalize-space()='Ok'] and not(@disabled)]")
        ));
        okButton.click();

        System.out.println("Join Date confirmed: "
                + joinDateDay + " " + joinDateMonth + " " + joinDateYear);

        // -------- Probation End Date --------
        // Open Probation End Date calendar
        WebElement probEndDateIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[2]/div[3]/div/form/div[13]/div/div")
        ));
        probEndDateIcon.click();
        System.out.println("Probation End Date calendar opened");

        // Click Year text to open year list
        WebElement endYearHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[@class='header__year']")
        ));
        endYearHeader.click();

        // Select Year
        WebElement endYearOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[@id='years']//span[normalize-space()='" + probEndDateYear + "']")
        ));
        endYearOption.click();
        System.out.println("Probation End Date Year selected: " + probEndDateYear);

        // Click Month text to open month list
        WebElement endMonthHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[@class='header__month']")
        ));
        endMonthHeader.click();

        // Select Month
        WebElement endMonthOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[@id='months']//span[normalize-space()='" + probEndDateMonth + "']")
        ));
        endMonthOption.click();
        System.out.println("Probation End Date Month selected: " + probEndDateMonth);

        // Select Day
        WebElement endDayOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[contains(@class,'dates')]//li"
                        + "[not(contains(@class,'dates__item--disabled'))]"
                        + "//div[normalize-space()='" + probEndDateDay + "']")
        ));
        endDayOption.click();
        System.out.println("Probation End Date Day selected: " + probEndDateDay);

        // Click OK
        WebElement endOkButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//p[normalize-space()='Ok'] and not(@disabled)]")
        ));
        endOkButton.click();

        System.out.println("Probation End Date confirmed: "
                + probEndDateDay + " " + probEndDateMonth + " " + probEndDateYear);

        // -------- Position --------
        WebElement positionDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-position")
        ));
        positionDropdown.click();

        WebElement positionOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" + position + "']")
        ));
        positionOption.click();
        System.out.println("Position selected: " + position);

        // -------- Level --------
        WebElement levelDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-level")
        ));
        levelDropdown.click();

        WebElement levelOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" + level + "']")
        ));
        levelOption.click();
        System.out.println("Level selected: " + level);

        // -------- Contract Type --------
        WebElement contractTypeDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-contractType")
        ));
        contractTypeDropdown.click();

        WebElement contractTypeOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" + contractType + "']")
        ));
        contractTypeOption.click();
        System.out.println("Contract Type selected: " + contractType);

        // -------- Reporting Manager --------
        WebElement reportingManagerDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-reportingManager")
        ));
        reportingManagerDropdown.click();

        WebElement reportingManagerOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" +  reportingManager + "']")
        ));
        reportingManagerOption.click();
        System.out.println("Reporting Manager selected: " +  reportingManager);

        allureScreenshot();
    }

    @And("admin fill bank details section {string}, {string}, {string}")
    public void admin_fill_bank_details_section(String bankName, String bankAccNo, String swiftCodeBranchName) {

        // -------- Bank Name --------
        WebElement bankNameDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-bank")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", bankNameDropdown);
        bankNameDropdown.click();

        WebElement bankNameOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" + bankName + "']")
        ));
        bankNameOption.click();
        System.out.println("Bank Name selected: " + bankName);

        // -------- Bank Account No --------
        WebElement bankAccNoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='bankAccountNo']")
        ));
        bankAccNoInput.clear();
        bankAccNoInput.sendKeys(bankAccNo);
        System.out.println("Bank Account No entered: " + bankAccNo);

        // -------- Swift Code Branch Name --------
        WebElement swiftCodeBranchNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='swiftCode']")
        ));
        swiftCodeBranchNameInput.clear();
        swiftCodeBranchNameInput.sendKeys(swiftCodeBranchName);
        System.out.println("Swift Code Branch Name entered: " + swiftCodeBranchName);

        allureScreenshot();
    }

    @And("admin fill contributions section {string}, {string}, {string}")
    public void admin_fill_contributions_section(String epfNo, String incomeTaxNo, String socsoNo) {

        // -------- EPF No --------
        WebElement epfNoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='epfNo']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", epfNoInput);
        epfNoInput.clear();
        epfNoInput.sendKeys(epfNo);
        System.out.println("EPF No entered: " + epfNo);

        // -------- Income Tax No --------
        WebElement incomeTaxNoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='incomeTaxNo']")
        ));
        incomeTaxNoInput.clear();
        incomeTaxNoInput.sendKeys(incomeTaxNo);
        System.out.println("Income Tax No entered: " + incomeTaxNo);

        // -------- Socso No --------
        WebElement socsoNoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='socsoNo']")
        ));
        socsoNoInput.clear();
        socsoNoInput.sendKeys(socsoNo);
        System.out.println("Socso No entered: " + socsoNo);

        allureScreenshot();
    }

    @And("admin click add button")
    public void admin_click_add_button() {
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Add']]")));
        addButton.click();
        System.out.println("Add button clicked");
    }

    @And("a successful toast message should display")
    public void aSuccessfulToastMessageShouldDisplay() {
        By successToastLocator = By.xpath(
                "//div[@id='app-alert' and contains(@class,'app-alert--active')]//p[@class='alert__text']"
        );

        // Wait until the toast is visible
        WebElement successToast = wait.until(
                ExpectedConditions.visibilityOfElementLocated(successToastLocator)
        );

        // Expected toast message
        String expectedMessage = "Invitation link will only be sent to members once policy is In Force.";

        // Assert that the toast text matches exactly
        Assert.assertEquals(
                successToast.getText().trim(),
                expectedMessage,
                "Success toast message text is incorrect"

        );

        // Wait until the toast disappears
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("app-alert")));
    }

    @And("new employee check email")
    public void newEmployeeCheckEmail() throws InterruptedException {
        // Store the current window handle
        mainWindowHandle = driver.getWindowHandle();

        // Open a new tab
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://mailsac.com/login");
        Thread.sleep(2000);

        // Login
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("nini44");
        driver.findElement(By.name("password")).sendKeys("matXis-susqut-dyxhi2");

        WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[1]/div[2]/div[1]/div/div/div/form/button")));
        signInButton.click();

        // Open email
        WebElement email = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"__next\"]/div[1]/div/div/div/div/div[1]/div/div/div/div/div/input[1]")));
        String generatedEmail = (String) ScenarioContext.getContext("EMP_EMAIL");
        String emailPrefix = generatedEmail.split("@")[0];

        email.sendKeys(emailPrefix,Keys.ENTER);

        allureScreenshot();

        // Search email
    }

    @And("employee click on the attached link in the email")
    public void employeeClickOnTheAttachedLinkInTheEmail() {

        // Click first email in inbox
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".row")));
        driver.findElements(By.cssSelector(".row")).get(0).click();

        System.out.println("Email opened");
        allureScreenshot();

        // Wait for Unblock button
        WebElement unblockBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(.,'Unblock links and images')]")
        ));

        // Save current URL before clicking
        String urlBefore = driver.getCurrentUrl();

        unblockBtn.click();
        System.out.println("Unblock button clicked");

        // Wait for navigation or new tab
        wait.until(driver ->
                driver.getWindowHandles().size() > 1 ||
                        !driver.getCurrentUrl().equals(urlBefore)
        );

        // Switch if new tab opened
        if (driver.getWindowHandles().size() > 1) {
            for (String window : driver.getWindowHandles()) {
                driver.switchTo().window(window);
            }
            System.out.println("Switched to new tab after unblock");
        } else {
            System.out.println("Stayed in same tab after unblock");
        }

        allureScreenshot();

        // ===== Activation link =====

        WebElement employeeLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(.,'here') or contains(@href,'http')]")
        ));
        // Save existing tabs BEFORE clicking
        Set<String> oldWindows = driver.getWindowHandles();

        // Click activation link
        employeeLink.click();

        System.out.println("Employee activation link clicked");

        // Wait for a new tab OR URL change
        //wait.until(driver ->
        //        driver.getWindowHandles().size() > oldWindows.size()
        //);

// Find the NEW tab
        Set<String> newWindows = driver.getWindowHandles();
        newWindows.removeAll(oldWindows);

        if (!newWindows.isEmpty()) {
            String newTab = newWindows.iterator().next();
            driver.switchTo().window(newTab);
            System.out.println("Switched to Create Password tab");
        } else {
            System.out.println("Activation opened in same tab");
        }

        wait.until(ExpectedConditions.urlContains("create-password"));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("create-password"),
                "Employee was not redirected to Create Password page. Current URL: " + currentUrl
        );

        System.out.println("Redirected to Create Password page successfully");
    }

    @And("employee enter {string} and {string}")
    public void employeeEnterPasswordAndConfirmPassword(String password, String confirmPassword) {
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
        passwordField.sendKeys(password);

        ScenarioContext.setContext("EMP_PASSWORD", password);

        WebElement confirmPasswordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("confirmPassword")));
        confirmPasswordField.sendKeys(confirmPassword);
    }

    @And("employee click on Create Password button")
    public void employeeClickOnCreatePasswordButton() {
        WebElement createPasswordButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div[2]/div/main/div[2]/form/div[3]/button")));
        createPasswordButton.click();
    }

    @And("new employee check and retrieve OTP")
    public void newEmployeeCheckEmailForOtp() throws InterruptedException {
        // Store the current window handle
        mainWindowHandle = driver.getWindowHandle();

        // Open a new tab
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://mailsac.com/login");
        Thread.sleep(2000);

        // Open email
        WebElement email = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"__next\"]/div[1]/div/div/div/div/div[1]/div/div/div/div/div/input[1]")));
        String generatedEmail = (String) ScenarioContext.getContext("EMP_EMAIL");
        String emailPrefix = generatedEmail.split("@")[0];

        Thread.sleep(4000);
        email.sendKeys(emailPrefix,Keys.ENTER);

        allureScreenshot();

        retrievedOTP = null;

        driver.navigate().refresh();

        List<WebElement> emails = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector("tr.clickable.ng-scope")
                )
        );

        WebElement latestEmail = emails.get(0);
        latestEmail.click();

        WebElement otpElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("/html/body/div/div[2]/div[1]/div/div[2]/div/table/tbody/tr[2]/td[2]/div[2]/div[2]/table/tbody/tr[6]/td/b")
                )
        );

        // Get OTP directly
        retrievedOTP = otpElement.getAttribute("textContent").trim();

        System.out.println("OTP Retrieved: " + retrievedOTP);

        // Enter OTP into boxes
        driver.switchTo().window(mainWindowHandle);

        String otp = retrievedOTP;

        for (int i = 0; i < otp.length(); i++) {
            WebElement otpField = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.name("otp-" + i)
                    )
            );
            otpField.sendKeys(Character.toString(otp.charAt(i)));
        }

        allureScreenshot();

    }

    @Then("User should redirect to hr portal")
    public void userShouldRedirectToHrPortal() {
        // Wait until URL contains expected path
        wait.until(ExpectedConditions.urlContains("https://hrms.uat.directintegrate.com/"));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("https://hrms.uat.directintegrate.com/"),
                "Employee was not redirected to Employee HR Portal Log In page. Current URL: " + currentUrl
        );

        System.out.println("Redirected to HR Portal Log In page successfully");
        allureScreenshot();

        String generatedPassword = (String) ScenarioContext.getContext("EMP_PASSWORD");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email"))).sendKeys(generatedEmail);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password"))).sendKeys(generatedPassword);
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Login']]")));
        loginButton.click();

        ExternalFunction.waitForLoaderToDisappear(driver);
    }

    @When("User selects level {string} in user management")
    public void userSelectsStatus(String level) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Click dropdown
        WebElement statusDropdown = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("mui-component-select-level")
                )
        );
        statusDropdown.click();

        // Wait menu appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("ul[role='listbox']")
        ));

        // Select option
        WebElement option = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//ul[@role='listbox']//li[contains(.,'" + level + "')]")
                )
        );

        option.click();
        System.out.println("Level clicked");
    }

    private String normalizeCell(String value) {
        if (value == null) return "";

        value = value.trim().toLowerCase();
        value = value.replace("\n", ", ");
        value = value.replaceAll("\\d{1,2}:\\d{2}\\s?(am|pm)", "");
        value = value.replaceAll("(\\d{2}/\\d{2})/\\d{4}", "$1");

        // Split and sort parts
        List<String> parts = Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .sorted()
                .collect(Collectors.toList());

        return String.join(", ", parts);
    }

    @And("exported file should contain only filtered data and visible columns in User Management")
    public void exportedFileShouldContainOnlyFilteredDataAndVisibleColumnsInUserManagement() throws Exception {

        // Wait and get the exported file
        File exportedFile = ExternalFunction.waitForExportedFile();

        ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);
        // Retrieve UI data
        List<String> uiHeaders = driver.findElements(
                        By.cssSelector("th.table__th:not(.table__th--action)")
                ).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text -> !text.isEmpty()) // ignore empty headers
                .collect(Collectors.toList());

        List<List<String>> uiTableData = ExternalFunction.getUITableData();

                /*getUITableData().stream()
                .map(row -> row.stream()
                        .map(String::trim)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
                */
        List<List<String>> excelData = ExternalFunction.readExcelData(exportedFile);

        if (excelData.isEmpty()) {
            Assert.fail("Exported Excel file is empty!");
        }

        List<String> excelHeaders = excelData.get(0).stream()
                .map(String::trim)
                .collect(Collectors.toList());

        List<List<String>> excelRows = excelData.subList(1, excelData.size()).stream()
                .map(row -> row.stream()
                        .map(String::trim)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        // Print retrieved data
        System.out.println("UI Headers: " + uiHeaders);
        System.out.println("Excel Headers: " + excelHeaders);

        // Compare
        Assert.assertEquals(
                excelHeaders.stream().map(String::toLowerCase).collect(Collectors.toList()),
                uiHeaders.stream().map(String::toLowerCase).collect(Collectors.toList()),
                "FAIL: Exported columns do not match visible UI columns"
        );
        System.out.println("PASS: Exported columns match UI headers.");

        Assert.assertEquals(
                excelRows.size(),
                uiTableData.size(),
                "FAIL: Exported row count does not match filtered UI row count"
        );
        System.out.println("PASS: Exported row count matches filtered UI row count.");

        for (int i = 0; i < uiTableData.size(); i++) {

            List<String> excelRow = excelRows.get(i).stream()
                    .map(this::normalizeCell)
                    .collect(Collectors.toList());

            List<String> uiRow = uiTableData.get(i).stream()
                    .map(this::normalizeCell)
                    .collect(Collectors.toList());

            Assert.assertEquals(
                    excelRow,
                    uiRow,
                    "FAIL: Mismatch found in exported row " + (i + 1)
            );
        }

        System.out.println("PASS: All exported rows match the filtered UI data.");

        allureScreenshot();
    }

    @And("user click on action button on Employee table")
    public void userClickOnActionButton() {

        ExternalFunction.waitForLoaderToDisappear(driver);

        By actionBtnLocator = By.xpath(
                "//button[contains(@class,'table__action')]"
        );

        WebElement actionBtn = wait.until(
                ExpectedConditions.elementToBeClickable(actionBtnLocator)
        );

        actionBtn.click();

        By editButtonLocator = By.xpath("//li[normalize-space()='Edit']");

        WebElement editButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(editButtonLocator)
        );

        wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
    }


    @And("employee should be able to view dashboard")
    public void employeeShouldBeAbleToViewDashboard() {
        WebElement dashboardElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[1]/div[2]/main/div[2]/header/div/h1")
        ));

        AssertJUnit.assertTrue("Dashboard element not visible.", dashboardElement.isDisplayed());

        System.out.println("Dashboard loaded successfully.");
    }

    @And("employee input onboarding {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}")
    public void employeeInputOnboardingDetails(String nickname, String address1, String address2, String state, String region, String postcode, String emergencyFullName, String emergencyRelationship, String emergencyMobile) {

        WebElement modalTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[text()='Please fill in the required informations']")
        ));

        modalTitle.isDisplayed();

        // -------- Nickname --------
        if (nickname.contains("[RandomUUID]")) {
            String uuid = java.util.UUID.randomUUID().toString().replaceAll("[^A-Za-z]", "");
            uuid = uuid.substring(0, 3); // take first 8 letters
            nickname = nickname.replace("[RandomUUID]", uuid);
        }

        WebElement nicknameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='nickname']")
        ));
        nicknameInput.clear();
        nicknameInput.sendKeys(nickname);
        System.out.println("Full Name entered: " + nickname);

        // -------- Address 1 --------
        WebElement correspondenceAddress1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='correspondenceAddress1']")
        ));
        correspondenceAddress1.clear();
        correspondenceAddress1.sendKeys(address1);
        System.out.println("Correspondence Address 1 entered: " + address1);

        // -------- Address 2 --------
        WebElement correspondenceAddress2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='correspondenceAddress2']")
        ));
        correspondenceAddress2.clear();
        correspondenceAddress2.sendKeys(address2);
        System.out.println("Correspondence Address 2 entered: " + address2);

        // -------- State --------
        WebElement stateDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-state")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", stateDropdown);
        stateDropdown.click();

        WebElement stateOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" + state + "']")
        ));
        stateOption.click();
        System.out.println("State selected: " + state);

        // -------- Region --------
        WebElement regionDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-region")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", regionDropdown);
        regionDropdown.click();

        WebElement regionOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" + region + "']")
        ));
        regionOption.click();
        System.out.println("Region selected: " + region);

        // -------- Postcode --------
        WebElement postcodeDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-postcode")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", postcodeDropdown);
        postcodeDropdown.click();

        WebElement postcodeOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" + postcode + "']")
        ));
        postcodeOption.click();
        System.out.println("Postcode selected: " + postcode);

        //--------- Same as Residential Address checkbox
        WebElement permanentAddress = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[@name='permanentAddress']")
        ));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", permanentAddress);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", permanentAddress);

        // -------- Emergency Name --------
        WebElement emergencyName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='emergencyContactFullName']")
        ));
        emergencyName.clear();
        emergencyName.sendKeys(emergencyFullName);
        System.out.println("Emergency Full Name entered: " + emergencyFullName);

        // -------- Relationship --------
        WebElement relationshipDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-emergencyContactRelationship")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", relationshipDropdown);
        relationshipDropdown.click();

        WebElement relationshipOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" + emergencyRelationship + "']")
        ));
        relationshipOption.click();
        System.out.println("Relationship selected: " + emergencyRelationship);

        // -------- Mobile No --------
        WebElement emergencyMobileNoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='emergencyContactMobileNo']")
        ));
        emergencyMobileNoInput.clear();
        emergencyMobileNoInput.sendKeys(emergencyMobile);
        System.out.println("Mobile No entered: " + emergencyMobile);

        // -------- Emergency Name (optional) bug, to be remove in the future --------
        WebElement emergencyName2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='emergencyContactFullName2']")
        ));
        emergencyName2.clear();
        emergencyName2.sendKeys(emergencyFullName);
        System.out.println("Emergency Full Name entered: " + emergencyFullName);
    }

    @And("employee fill health declaration and consent")
    public void employeeFillHealthDeclarationAndConsent() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //---------- Dietary
        WebElement dietary = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[contains(text(),'Any dietary preferences')]//ancestor::div[@class='app-button-input']//button[normalize-space()='No']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dietary);
        dietary.click();

        //---------- Allergies
        WebElement allergies = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[contains(text(),'Any food allergies')]//ancestor::div[@class='app-button-input']//button[normalize-space()='No']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", allergies);
        allergies.click();

        //---------- Medical
        WebElement medical = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[contains(text(),'Any medical condition')]//ancestor::div[@class='app-button-input']//button[normalize-space()='No']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", medical);
        medical.click();

        //-------- Medication
        WebElement medication = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[contains(text(),'any long-term medication')]//ancestor::div[@class='app-button-input']//button[normalize-space()='No']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", medication);
        medication.click();

        //--------accurateInfoCheckbox
        WebElement accurateInfoCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[@name='infoConfirmed']")
        ));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", accurateInfoCheckbox);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accurateInfoCheckbox);

        //-------- acknowledgePolicyCheckbox
        WebElement acknowledgePolicyCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[@name='acknowledgePolicy']")
        ));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", acknowledgePolicyCheckbox);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acknowledgePolicyCheckbox);

    }

    @And("employee click submit button")
    public void employeeClickSubmitButton() {
        WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//p[contains(text(),'Submit')]]")
        ));
        submit.click();

        wait.until(ExpectedConditions.stalenessOf(submit));

        WebElement successModal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("welcome-onboard-modal")
        ));

        WebElement title = driver.findElement(
                By.xpath("//p[text()='Welcome Onboard!']")
        );

        Assert.assertTrue(title.isDisplayed(), "FAIL: Success modal not displayed");

        WebElement okayBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//p[text()='Okay']]")
        ));
        okayBtn.click();


    }

    @And("admin edit {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}")
    public void adminEditPersonalInfo(String fullName, String nickname, String residentialAddress1, String residentialAddress2, String state, String region, String postcode, String emergencyFullName, String emergencyRelationship, String emergencyMobile) {

        // -------- Full Name -------
        if (fullName.contains("[RandomUUID]")) {
            String uuid = java.util.UUID.randomUUID().toString().replaceAll("[^A-Za-z]", "");
            uuid = uuid.substring(0, 3); // take first 8 letters
            fullName = fullName.replace("[RandomUUID]", uuid);
        }

        WebElement fullNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='name']")
        ));
        fullNameInput.clear();
        fullNameInput.sendKeys(fullName);
        System.out.println("Full Name entered: " + fullName);

        // -------- Nickname --------
        if (nickname.contains("[RandomUUID]")) {
            String uuid = java.util.UUID.randomUUID().toString().replaceAll("[^A-Za-z]", "");
            uuid = uuid.substring(0, 3); // take first 8 letters
            nickname = nickname.replace("[RandomUUID]", uuid);
        }

        WebElement nicknameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='nickname']")
        ));
        nicknameInput.clear();
        nicknameInput.sendKeys(nickname);
        System.out.println("Nickname entered: " + nickname);

        // -------- Address 1 --------
        if (residentialAddress1.contains("[RandomUUID]")) {
            String uuid = java.util.UUID.randomUUID().toString().replaceAll("[^A-Za-z]", "");
            uuid = uuid.substring(0, 3); // take first 8 letters
            residentialAddress1 = residentialAddress1.replace("[RandomUUID]", uuid);
        }

        WebElement correspondenceAddress1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='addressLine1']")
        ));
        correspondenceAddress1.clear();
        correspondenceAddress1.sendKeys(residentialAddress1);
        System.out.println("Correspondence Address 1 entered: " + residentialAddress1);

        // -------- Address 2 --------
        if (residentialAddress2.contains("[RandomUUID]")) {
            String uuid = java.util.UUID.randomUUID().toString().replaceAll("[^A-Za-z]", "");
            uuid = uuid.substring(0, 3); // take first 8 letters
            residentialAddress2 = residentialAddress2.replace("[RandomUUID]", uuid);
        }

        WebElement correspondenceAddress2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='addressLine2']")
        ));
        correspondenceAddress2.clear();
        correspondenceAddress2.sendKeys(residentialAddress2);
        System.out.println("Correspondence Address 2 entered: " + residentialAddress2);

        // -------- State --------
        WebElement stateDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-state")
        ));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", stateDropdown
        );
        stateDropdown.click();

        // Wait for dropdown options to appear (IMPORTANT)
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[@role='option']")
        ));

        WebElement stateOption = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//li[@role='option' and normalize-space()='" + state + "']")
        ));

        // Use JS click instead of normal click
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", stateOption);

        System.out.println("State selected: " + state);

        // -------- Region --------
        WebElement regionDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-region")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", regionDropdown);
        regionDropdown.click();

        WebElement regionOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" + region + "']")
        ));
        regionOption.click();
        System.out.println("Region selected: " + region);

        // -------- Postcode --------
        WebElement postcodeDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-postcode")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", postcodeDropdown);
        postcodeDropdown.click();

        WebElement postcodeOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" + postcode + "']")
        ));
        postcodeOption.click();
        System.out.println("Postcode selected: " + postcode);

        // -------- Emergency Name --------
        if (emergencyFullName.contains("[RandomUUID]")) {
            String uuid = java.util.UUID.randomUUID().toString().replaceAll("[^A-Za-z]", "");
            uuid = uuid.substring(0, 3); // take first 8 letters
            emergencyFullName = emergencyFullName.replace("[RandomUUID]", uuid);
        }

        WebElement emergencyName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='emergencyContactsFullName']")
        ));
        emergencyName.clear();
        emergencyName.sendKeys(emergencyFullName);
        System.out.println("Emergency Full Name entered: " + emergencyFullName);

        // -------- Relationship --------
        WebElement relationshipDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-emergencyContactsRelationship")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", relationshipDropdown);
        relationshipDropdown.click();

        WebElement relationshipOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" + emergencyRelationship + "']")
        ));
        relationshipOption.click();
        System.out.println("Relationship selected: " + emergencyRelationship);

        // -------- Mobile No --------
        WebElement emergencyMobileNoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='emergencyContactsMobileNo']")
        ));
        emergencyMobileNoInput.clear();
        emergencyMobileNoInput.sendKeys(emergencyMobile);
        System.out.println("Mobile No entered: " + emergencyMobile);


        ScenarioContext.setContext("fullName", fullName);
        ScenarioContext.setContext("nickname", nickname);
        ScenarioContext.setContext("address1", residentialAddress1);
        ScenarioContext.setContext("address2", residentialAddress2);
        ScenarioContext.setContext("state", state);
        ScenarioContext.setContext("region", region);
        ScenarioContext.setContext("postcode", postcode);
        ScenarioContext.setContext("emergencyName", emergencyFullName);
        ScenarioContext.setContext("relationship", emergencyRelationship);
        ScenarioContext.setContext("mobile", emergencyMobile);
    }

    @Then("admin click Save button")
    public void adminClickSaveButton() {
        WebElement saveButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Save']"))
        );
        saveButton.click();
    }

    @And("update successful toast message display")
    public void updateSuccessfulToastMessageDisplay() {
        By successToastLocator = By.xpath(
                "//div[@id='app-alert' and contains(@class,'app-alert--active')]//p[@class='alert__text']"
        );

        // Wait until the toast is visible
        WebElement successToast = wait.until(
                ExpectedConditions.visibilityOfElementLocated(successToastLocator)
        );

        // Expected toast message
        String expectedMessage = "Employee has been updated successfully";

        // Assert that the toast text matches exactly
        Assert.assertEquals(
                successToast.getText().trim(),
                expectedMessage,
                "Success toast message text is incorrect"

        );

        // Wait until the toast disappears
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("app-alert")));
    }

    @And("page should update to the latest input")
    public void pageShouldUpdateToTheLatestInput() {

         String expectedFullName = (String) ScenarioContext.getContext("fullName");
         String expectedNickname = (String) ScenarioContext.getContext("nickname");
         String expectedResidentialAddress1 = (String) ScenarioContext.getContext("address1");
         String expectedResidentialAddress2 = (String) ScenarioContext.getContext("address2");
         String expectedState = (String) ScenarioContext.getContext("state");
         String expectedRegion = (String) ScenarioContext.getContext("region");
         String expectedPostcode = (String) ScenarioContext.getContext("postcode");
         String expectedEmergencyFullName = (String) ScenarioContext.getContext("emergencyName");
         String expectedEmergencyRelationship = (String) ScenarioContext.getContext("relationship");
         String expectedEmergencyMobile = (String) ScenarioContext.getContext("mobile");

         // -------- Verify Full Name
         WebElement fullNameText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                 By.xpath("//input[@name='name']")
         ));
         String actualFullName = fullNameText.getAttribute("value");

        Assert.assertTrue(
                actualFullName.equalsIgnoreCase(expectedFullName),
                "Full Name not updated!"
        );

         // -------- Verify Nickname
         WebElement nicknameText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                 By.xpath("//input[@name='nickname']")
         ));
         String actualNickname = nicknameText.getAttribute("value");

         Assert.assertEquals(actualNickname, expectedNickname, "Nickname not updated!");

        // -------- Verify address1
        WebElement address1Text = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='addressLine1']")
        ));
        String actualAddress1 = address1Text.getAttribute("value");

        Assert.assertEquals(actualAddress1, expectedResidentialAddress1, "Address Line 1 not updated!");

        // -------- Verify address2
        WebElement address2Text = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='addressLine2']")
        ));
        String actualAddress2 = address2Text.getAttribute("value");

        Assert.assertEquals(actualAddress2, expectedResidentialAddress2, "Address Line 2 not updated!");

        // -------- Verify State
        WebElement stateText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("mui-component-select-state")
        ));

        String actualState = stateText.getText();

        Assert.assertTrue(
                actualState.trim().equalsIgnoreCase(expectedState.trim()),
                "State not updated!"
        );

        // -------- Verify Region
        WebElement regionText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("mui-component-select-region")
        ));

        String actualRegion = regionText.getText();

        Assert.assertTrue(
                actualRegion.trim().equalsIgnoreCase(expectedRegion.trim()),
                "Region not updated!"
        );

        // -------- Verify Postcode
        WebElement postcodeText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("mui-component-select-postcode")
        ));

        String actualPostcode = postcodeText.getText();

        Assert.assertTrue(
                actualPostcode.trim().equalsIgnoreCase(expectedPostcode.trim()),
                "Postcode not updated!"
        );

        // -------- Verify Emergency Name
        WebElement emergencyNameText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='emergencyContactsFullName']")
        ));
        String actualEmergencyName = emergencyNameText.getAttribute("value");

        Assert.assertEquals(actualEmergencyName, expectedEmergencyFullName, "Emergency Full Name not updated!");

        // -------- Verify Emergency Relationship
        WebElement emergencyRelationshipText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("mui-component-select-emergencyContactsRelationship")
        ));

        String actualEmergencyRelationship = emergencyRelationshipText.getText();

        Assert.assertTrue(
                actualEmergencyRelationship.trim().equalsIgnoreCase(expectedEmergencyRelationship.trim()),
                "Relationship not updated!"
        );

        // -------- Verify Emergency Contact
        WebElement emergencyContactText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='emergencyContactsMobileNo']")
        ));
        String actualEmergencyContact = emergencyContactText.getAttribute("value");

        Assert.assertEquals(actualEmergencyContact, expectedEmergencyMobile, "Emergency Contact not updated!");

    }

    @And("admin click on the employee info tab")
    public void adminClickOnTheEmployeeInfoTab() {
        WebElement employeeInfoTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(@class,'tabs__tab') and normalize-space()='Employee Info']")
        ));

        employeeInfoTab.click();
    }

    @And("admin edit {string} and {string}")
    public void adminEditPositionAndLevel(String position, String level) {
        // -------- Position --------
        WebElement positionDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-position")
        ));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", positionDropdown
        );
        positionDropdown.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[@role='option']")
        ));

        WebElement positionOption = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//li[@role='option' and normalize-space()='" + position + "']")
        ));

        // Use JS click instead of normal click
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", positionOption);

        System.out.println("Position selected: " + position);

        // -------- Level --------
        WebElement levelDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-level")
        ));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", levelDropdown
        );
        levelDropdown.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[@role='option']")
        ));

        WebElement levelOption = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//li[@role='option' and normalize-space()='" + level + "']")
        ));

        // Use JS click instead of normal click
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", levelOption);

        System.out.println("Level selected: " + level);

        ScenarioContext.setContext("position", position);
        ScenarioContext.setContext("level", level);
    }

    @And("page should update the position and level")
    public void pageShouldUpdateThePositionAndLevel() {

        String expectedPosition = (String) ScenarioContext.getContext("position");
        String expectedLevel = (String) ScenarioContext.getContext("level");

        // -------- Verify Position
        WebElement positionText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("mui-component-select-position")
        ));

        String actualPosition = positionText.getText();

        Assert.assertTrue(
                actualPosition.trim().equalsIgnoreCase(expectedPosition.trim()),
                "Position not updated!"
        );

        // -------- Verify Level
        WebElement levelText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("mui-component-select-level")
        ));

        String actualLevel = levelText.getText();

        Assert.assertTrue(
                actualLevel.trim().equalsIgnoreCase(expectedLevel.trim()),
                "Level not updated!"
        );

    }

    @And("admin click on the document tab")
    public void adminClickOnTheDocumentTab() {
        WebElement employeeInfoTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(@class,'tabs__tab') and normalize-space()='Document']")
        ));

        employeeInfoTab.click();
    }

    @Then("admin upload document {string}")
    public void adminUploadDocument(String filePath) {

        WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));

        fileInput.sendKeys(filePath);

        By successToastLocator = By.xpath(
                "//div[@id='app-alert' and contains(@class,'app-alert--active')]//p[@class='alert__text']"
        );

        WebElement successToast = wait.until(
                ExpectedConditions.visibilityOfElementLocated(successToastLocator)
        );

        String expectedMessage = "File has been uploaded successfully";

        Assert.assertEquals(
                successToast.getText().trim(),
                expectedMessage,
                "Success toast message text is incorrect"

        );

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("app-alert")));

    }

    @And("admin download document")
    public void adminDownloadDocument() {
        By actionBtnLocator = By.xpath(
                "//button[contains(@class,'table__action')]"
        );

        WebElement actionBtn = wait.until(
                ExpectedConditions.elementToBeClickable(actionBtnLocator)
        );

        actionBtn.click();

        By downloadButtonLocator = By.xpath("//li[normalize-space()='Download']");

        WebElement downloadButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(downloadButtonLocator)
        );

        downloadButton.click();
    }

    @And("admin delete document")
    public void adminDeleteDocument() {
        By actionBtnLocator = By.xpath(
                "//button[contains(@class,'table__action')]"
        );

        WebElement actionBtn = wait.until(
                ExpectedConditions.elementToBeClickable(actionBtnLocator)
        );

        actionBtn.click();

        By deleteButtonLocator = By.xpath("//li[normalize-space()='Delete']");

        WebElement deleteButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(deleteButtonLocator)
        );

        deleteButton.click();

        By successToastLocator = By.xpath(
                "//div[@id='app-alert' and contains(@class,'app-alert--active')]//p[@class='alert__text']"
        );

        WebElement successToast = wait.until(
                ExpectedConditions.visibilityOfElementLocated(successToastLocator)
        );

        String expectedMessage = "File has been deleted successfully";

        Assert.assertEquals(
                successToast.getText().trim(),
                expectedMessage,
                "Success toast message text is incorrect"

        );

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("app-alert")));
    }

    @When("admin click on Admin List")
    public void adminClickOnAdminList() {
        // Wait for loader overlay to disappear (using your helper)
        ExternalFunction.waitForLoaderToDisappear(driver);

        // Wait for the Company Benefit element to be ready
        WebElement allowanceManagementSection = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//p[normalize-space(text())='Admin List']")
                )
        );

        wait.until(ExpectedConditions.visibilityOf(allowanceManagementSection));
        // Attempt click (with JS fallback)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(allowanceManagementSection));
            allowanceManagementSection.click();
            System.out.println("Clicked on 'Admin List' normally.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, using JavaScript click instead...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", allowanceManagementSection);
            js.executeScript("arguments[0].click();", allowanceManagementSection);
            System.out.println("Clicked on 'Admin List' via JavaScript.");
        }
        // Wait again after navigation
        try {
            ExternalFunction.waitForLoaderToDisappear(driver);
            System.out.println("Page finished loading after navigation.");
        } catch (TimeoutException e) {
            System.out.println("Loader did not disappear after navigation, proceeding...");
        }
    }

    @And("admin click on Add New button")
    public void adminClickOnAddNewButton() {
        WebElement addAdminButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Add New']"))
        );
        addAdminButton.click();
    }

    @And("admin select {string} from the dropdown")
    public void adminSelectNameFromTheDropdown(String admin) {

        // -------- Select new Admin --------
        WebElement adminDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("mui-component-select-name")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", adminDropdown);
        adminDropdown.click();

        WebElement adminOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option' and normalize-space()='" + admin + "']")
        ));
        adminOption.click();
        System.out.println("New Admin selected: " + admin);
    }

    @Then("click Confirm button")
    public void clickConfirmButton() {
        WebElement confirmButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Confirm']"))
        );
        confirmButton.click();
    }

    @And("success toast message shall display")
    public void successToastMessageShallDisplay() {
        By successToastLocator = By.xpath(
                "//div[@id='app-alert' and contains(@class,'app-alert--active')]//p[@class='alert__text']"
        );

        WebElement successToast = wait.until(
                ExpectedConditions.visibilityOfElementLocated(successToastLocator)
        );

        String expectedMessage = "New user created successfully";

        Assert.assertEquals(
                successToast.getText().trim(),
                expectedMessage,
                "Success toast message text is incorrect"

        );

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("app-alert")));
    }

    @And("admin click on the action button at the end of a row for {string}")
    public void adminClickOnTheActionButtonAtTheEndOfARow(String admin) {
        WebElement cell = findElementInPaginatedTable(
                driver,
                wait,
                By.xpath("//td"), // adjust if needed (e.g. specific column),
                admin,
                10
        );

        WebElement row = cell.findElement(By.xpath("./ancestor::tr"));

        WebElement actionBtn = row.findElement(
                By.xpath(".//button[contains(@class,'table__action')]")
        );

        wait.until(ExpectedConditions.elementToBeClickable(actionBtn)).click();

        WebElement removeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='Remove']")
        ));
        removeBtn.click();
    }

    @And("display success toast message with {string} name")
    public void displaySuccessToastMessage(String admin) {
        // Write code here that turns the phrase above into concrete actions
        By successToastLocator = By.xpath(
                "//div[@id='app-alert' and contains(@class,'app-alert--active')]//p[@class='alert__text']"
        );

        WebElement successToast = wait.until(
                ExpectedConditions.visibilityOfElementLocated(successToastLocator)
        );

        String expectedMessage = String.format("%s has been removed successfully", admin);

        Assert.assertEquals(
                successToast.getText().trim(),
                expectedMessage,
                "Success toast message text is incorrect"

        );

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("app-alert")));
    }
}
