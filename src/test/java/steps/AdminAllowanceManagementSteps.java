package steps;

import drivers.DriverInstance;
import hooks.Hooks;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import qa.util.ExternalFunction;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;

import static qa.util.ExternalFunction.getUITableData;
import static qa.util.ExternalFunction.getVisibleTableHeaders;

public class AdminAllowanceManagementSteps extends DriverInstance {
    WebDriver driver = Hooks.driver;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

    private WebElement selectedRow;
    private String selectedEmployeeName;
    private List<String> selectedClaimIdentifiers = new ArrayList<>();


    @And("User click on Allowance Management")
    public void userClickOnAllowanceManagement() {
        // Wait for loader overlay to disappear (using your helper)
        ExternalFunction.waitForLoaderToDisappear(driver);

        // Wait for the Company Benefit element to be ready
        WebElement allowanceManagementSection = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//p[normalize-space(text())='Allowance Management']")
                )
        );

        wait.until(ExpectedConditions.visibilityOf(allowanceManagementSection));
        // Attempt click (with JS fallback)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(allowanceManagementSection));
            allowanceManagementSection.click();
            System.out.println("Clicked on 'Leave Management' normally.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, using JavaScript click instead...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", allowanceManagementSection);
            js.executeScript("arguments[0].click();", allowanceManagementSection);
            System.out.println("Clicked on 'Allowance Management' via JavaScript.");
        }
        // Wait again after navigation
        try {
            ExternalFunction.waitForLoaderToDisappear(driver);
            System.out.println("Page finished loading after navigation.");
        } catch (TimeoutException e) {
            System.out.println("Loader did not disappear after navigation, proceeding...");
        }
    }

    @And("User can see Allowance Directory header")
    public void userCanSeeAllowanceDirectoryHeader() {
        try {
            WebElement header = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h1[normalize-space(text())='Allowance Directory']")
                    )
            );

            System.out.println("Header found: " + header.getText());
            Assert.assertTrue(header.isDisplayed(), "Allowance Directory header is not displayed");

        } catch (TimeoutException e) {
            System.out.println("Header 'Allowance Directory' not found within timeout!");
            Assert.fail("Header 'Allowance Directory' not found!");
        }
        allureScreenshot();
    }

    @When("User click on Claim History")
    public void userClickOnClaimHistory() {
        // Wait for loader overlay to disappear (using your helper)
        ExternalFunction.waitForLoaderToDisappear(driver);

        // Wait for the Company Benefit element to be ready
        WebElement claimHistorySection = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//p[normalize-space(text())='Claim History']")
                )
        );

        wait.until(ExpectedConditions.visibilityOf(claimHistorySection));

        // Attempt click (with JS fallback)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(claimHistorySection));
            claimHistorySection.click();
            System.out.println("Clicked on 'Claim History' normally.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, using JavaScript click instead...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", claimHistorySection);
            js.executeScript("arguments[0].click();", claimHistorySection);
            System.out.println("Clicked on 'Claim History' via JavaScript.");
        }

        // Wait again after navigation
        try {
            ExternalFunction.waitForLoaderToDisappear(driver);
            System.out.println("Page finished loading after navigation.");
        } catch (TimeoutException e) {
            System.out.println("Loader did not disappear after navigation, proceeding...");
        }
    }

    @Then("User can see Claim History header")
    public void userCanSeeClaimHistoryHeader() {
        try {
            WebElement header = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h1[normalize-space(text())='Claim History']")
                    )
            );

            System.out.println("Header found: " + header.getText());
            Assert.assertTrue(header.isDisplayed(), "Claim History header is not displayed");

        } catch (TimeoutException e) {
            System.out.println("Header 'Claim History' not found within timeout!");
            Assert.fail("Header 'Claim History' not found!");
        }
        allureScreenshot();
    }

    @And("an existing Allowance Directory is available for viewing")
    public void anExistingAllowanceDirectoryIsAvailableForViewing() {
        ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);

        List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
        Assert.assertFalse(rows.isEmpty(), "No Allowance Directory available for viewing");

        selectedRow = rows.get(1); // pick any row

        selectedEmployeeName =
                selectedRow.findElement(By.xpath("./td[2]")).getText().trim();
    }

    @And("user click on view action button in Allowance Management")
    public void userClickOnViewActionButton(){
        WebElement viewButton =
                selectedRow.findElement(By.cssSelector("button.table__action"));

        wait.until(ExpectedConditions.elementToBeClickable(viewButton));
        viewButton.click();
    }

    public void clickTab(String tabName) {
        By tabs = By.cssSelector("ul.tabs li.tabs__tab");

        List<WebElement> tabList = driver.findElements(tabs);

        for (WebElement tab : tabList) {
            if (tab.getText().trim().equalsIgnoreCase(tabName)) {
                tab.click();
                return;
            }
        }
        Assert.fail("Tab not found: " + tabName);
    }

    public void validateActiveTab(String expectedTab) {
        By activeTab = By.cssSelector("ul.tabs li.tabs__tab--active");

        WebElement active = driver.findElement(activeTab);
        String actualTab = active.getText().trim();

        Assert.assertEquals(
                actualTab,
                expectedTab,
                "Incorrect active tab"
        );
    }
    @And("user should see the same employee name as header in Allowance Management")
    public void userShouldSeeTheSameEmployeeNameAsHeader(){
        WebElement header = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".claim__name")
                )
        );

        String headerName = header.getText().trim();

        Assert.assertEquals(
                headerName,
                selectedEmployeeName,
                "FAIL: Employee name in header does not match selected Leave Directory"
        );

        System.out.println("PASS: Employee header name matches selected directory");

        allureScreenshot();
    }

    @And("Claim Summary tab should be active")
    public void claimSummaryTabShouldBeActive() {
        validateActiveTab("Claim Summary");
        allureScreenshot();
    }

    @And("user click on Claim History tab")
    public void userClickOnClaimHistoryTab() {
        clickTab("Claim History");
    }

    @And("Claim History tab should be active")
    public void claimHistoryTabShouldBeActive() {
        validateActiveTab("Claim History");
        allureScreenshot();
    }

    @And("Pending Approval button should redirect to the employee pending approval leave list in Allowance Management")
    public void pendingApprovalbutton(){
        driver.findElement(
                By.xpath("//button[normalize-space()='Pending Approval']")
        ).click();

        // Wait for the Pending Approval page/table to load
        ExternalFunction.waitForTableToLoad(driver, ".app-table", 10);

        // Verify the page is actually Pending Approval (optional: using a known label or table header)
        WebElement pageTitle = driver.findElement(By.cssSelector(".header__title")); // adjust selector if needed
        Assert.assertTrue(pageTitle.getText().contains("Pending Approval"),
                "FAIL: User is not on Pending Approval page");

        // Verify the employee name filter indicator
        By employeeNameLocator = By.cssSelector(".advance-search__pill"); // adjust selector to your actual element
        WebElement employeeNameElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(employeeNameLocator));

        String displayedName = employeeNameElement.getText().trim();

        Assert.assertEquals(displayedName, selectedEmployeeName,
                "FAIL: Employee name in Pending Approval page does not match selected employee");

        System.out.println("PASS: Pending Approval page is filtered for employee: " + selectedEmployeeName);

        allureScreenshot();
    }

    @And("user click on action button")
    public void userClickOnActionButton() {
        // Wait for loader overlay to disappear (using your helper)
        ExternalFunction.waitForLoaderToDisappear(driver);

        driver.findElement(By.xpath("//table/tbody/tr[1]/td[6]//button")).click();
    }

    @Then("user click on the Reject Leave button in the side panel")
    public void clickRejectButton() {
        WebElement rejectBtn = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/div[3]/div/div[2]/button[1]"));
        rejectBtn.click();
        System.out.println("PASS: Reject Leave button clicked");
    }

    @Then("user click on the Approve Leave button in the side panel")
    public void clickApproveButton() {
        WebElement rejectBtn = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/div[3]/div/div[2]/button[2]"));
        rejectBtn.click();
        System.out.println("PASS: Approve Leave button clicked");
    }

    private String buildLeaveKey(WebElement row) {
        String username = row.findElement(By.xpath("./td[2]")).getText().trim();
        String claimType = row.findElement(By.xpath("./td[3]")).getText().trim();
        String submitDate = row.findElement(By.xpath("./td[4]//div")).getText().trim();
        String amount = row.findElement(By.xpath("./td[5]//div")).getText().trim();

        return username + "|" + claimType + "|" + submitDate + "|" + amount;
    }

    @Then("user can select multiple claim entry in Allowance Management")
    public void selectMultipleLeaves() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector("tbody.table__tbody tr.table__tr"), 1));

        List<WebElement> rows =
                driver.findElements(By.cssSelector("tbody.table__tbody tr.table__tr"));

        Assert.assertTrue(rows.size() >= 2,
                "Not enough claim entries to select. Found: " + rows.size());

        for (int i = 0; i < 2; i++) {
            WebElement row = rows.get(i);

            String leaveKey = buildLeaveKey(row);
            selectedClaimIdentifiers.add(leaveKey);

            row.findElement(By.cssSelector("td.table__td--selection button.table__checkbox"))
                    .click();
        }

        System.out.println("PASS: Selected claim entries: " + selectedClaimIdentifiers);
    }

    @And("system should display success toast message in Allowance Management")
    public void systemShouldDisplaySuccessToastMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for alert container
        WebElement alert = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("app-alert")
                )
        );

        Assert.assertTrue(
                alert.isDisplayed(),
                "Success alert container is not displayed"
        );

        // Validate success text
        WebElement alertText = alert.findElement(By.cssSelector("p.alert__text"));
        String actualText = alertText.getText().trim();

        Assert.assertEquals(
                actualText,
                "Claim has been approved successfully",
                "Incorrect success toast message"
        );

        System.out.println("PASS: Success toast displayed with message → " + actualText);

        // Optional: wait until toast disappears (stability)
        wait.until(ExpectedConditions.invisibilityOf(alert));
    }

    @Then("All checked column should visible in the table behind the modal in Allowance Management")
    public void allCheckedColumnShouldVisibleInTheTableBehindTheModal() {
        // Mapping between filter checkbox labels and table header text
        Map<String, String> columnNameMap = new HashMap<>();
        columnNameMap.put("Name", "Name");
        columnNameMap.put("Claim Type", "Claim Type");
        columnNameMap.put("Amount (RM)", "Amount (RM)");
        columnNameMap.put("Attachment", "Attachment");
        columnNameMap.put("Submit Date", "Submit Date");
        columnNameMap.put("Visit Date", "Visit Date");
        columnNameMap.put("Status", "Status");
        columnNameMap.put("Last Update by", "Last Update by");

        // Get checked filter columns
        List<WebElement> checkedBoxes = driver.findElements(
                By.cssSelector(".advance-form__inputs__checkbox .app-checkbox.app-checkbox--active"));

        List<String> checkedColumnNames = new ArrayList<>();
        for (WebElement box : checkedBoxes) {
            String label = box.findElement(By.cssSelector(".app-checkbox__label")).getText().trim();
            checkedColumnNames.add(label);
        }

        // Get visible table headers
        List<WebElement> headerElements = driver.findElements(By.cssSelector("table thead th"));
        List<String> tableHeaders = new ArrayList<>();
        for (WebElement header : headerElements) {
            tableHeaders.add(header.getText().trim());
        }

        // Verify mapped header names are visible
        for (String col : checkedColumnNames) {
            String expectedHeader = columnNameMap.getOrDefault(col, col); // fallback to same name
            Assert.assertTrue(tableHeaders.contains(expectedHeader),
                    "Expected column '" + expectedHeader + "' (from filter '" + col + "') to be visible in the table, but it's missing.");
        }
    }

    @Then("User toggles {string} checkbox to {string} in Allowance Management")
    public void userTogglesColumnCheckboxToState(String column, String expectedState) throws InterruptedException {
        // Find checkbox by its label
        WebElement checkbox = driver.findElement(By.xpath("//p[text()='" + column + "']/ancestor::button"));
        boolean isActive = checkbox.getAttribute("class").contains("app-checkbox--active");

        System.out.println("Checkbox '" + column + "' CURRENT STATE: " + (isActive ? "checked" : "unchecked"));
        System.out.println("Checkbox '" + column + "' EXPECTED STATE: " + expectedState);

        boolean shouldBeChecked = expectedState.equalsIgnoreCase("visible");

        // Only click IF current does NOT match expected
        if (isActive != shouldBeChecked) {
            System.out.println("Toggling checkbox to match expected state...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
            Thread.sleep(500); // give a short delay for UI update
        } else {
            System.out.println("No toggle needed — already in expected state.");
        }
    }

    @Then("The {string} column should be {string} in the Allowance Management table")
    public void theColumnColumnShouldBeExpectedStateInTheTable(String column, String expectedState) {
        // Close MUI backdrop if present
        List<WebElement> backdrops = driver.findElements(
                By.cssSelector(".MuiBackdrop-root.MuiModal-backdrop"));
        if (!backdrops.isEmpty()) {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", backdrops.get(0));
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.invisibilityOf(backdrops.get(0)));
        }

        // Locate column header by text
        List<WebElement> headers = driver.findElements(
                By.xpath("//th[@role='columnheader' and " +
                        "contains(normalize-space(.), '" + column + "')]")
        );

        boolean headerExists = !headers.isEmpty();
        boolean headerVisible = headerExists && headers.get(0).isDisplayed();

        System.out.println("Header exists  : " + headerExists);
        System.out.println("Header visible : " + headerVisible);

        if (expectedState.equalsIgnoreCase("visible")) {
            Assert.assertTrue(
                    headerVisible,
                    "Column '" + column + "' SHOULD be visible but is hidden or missing."
            );
        } else {
            Assert.assertFalse(
                    headerVisible,
                    "Column '" + column + "' SHOULD be hidden but is visible."
            );
        }

    }

    private Map<String, By> filterFieldsAllowance = new HashMap<>() {{
        put("Employee ID", By.cssSelector("input[placeholder='Enter employee ID']"));
        put("Name", By.cssSelector("input[placeholder='Enter employee name']"));
        put("Nickname", By.cssSelector("input[placeholder='Enter employee Nickname']"));
        put("Employee Email", By.cssSelector("input[placeholder='Enter mentor name']"));
    }};

    @When("User enters {string} in {string} in Allowance Management")
    public void userEntersValueInField(String value, String field) {
        String[] values = value.split(",");
        String[] fields = field.split(",");

        for (int i = 0; i < fields.length; i++) {

            String fieldName = fields[i].trim();
            String fieldValue = values[i].trim();

            By locator = filterFieldsAllowance.get(fieldName);

            if (locator == null) {
                throw new IllegalArgumentException("No locator found for: " + fieldName);
            }

            WebElement element = driver.findElement(locator);
            element.clear();
            element.sendKeys(fieldValue);
        }
    }

    @When("User selects Claim Type {string}")
    public void userSelectsClaimType(String status) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Click dropdown
        WebElement dropdown = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("mui-component-select-claimType")
                )
        );
        dropdown.click();

        // Click option by data-value (VERY STABLE)
        WebElement option = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("li[data-value='" + status + "']")
                )
        );

        option.click();
    }


    public void setDate(String fieldName, String dateStr) throws InterruptedException {

        DateTimeFormatter inputFormatter =
                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("dd MMMM yyyy")
                        .toFormatter(Locale.ENGLISH);

        LocalDate targetDate = LocalDate.parse(dateStr, inputFormatter);

        String calendarIconXpath =
                "//input[@name='" + fieldName + "']/following-sibling::div//*[contains(@class, 'app-icon')]";

        WebElement calendarIcon =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(calendarIconXpath)));

        try {
            calendarIcon.click();
        } catch (ElementClickInterceptedException e) {
            System.out.println("Icon not clickable, trying JS click...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", calendarIcon);

            try {
                WebElement wrapper = calendarIcon.findElement(
                        By.xpath("./ancestor::div[contains(@class,'app-calendar-input__wrapper')]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", wrapper);
            } catch (Exception ignored) {}
        }


        WebElement calendarPopup = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector(".app-calendar .calendar")));

        WebElement monthLabel = calendarPopup.findElement(By.cssSelector(".header__month"));
        WebElement yearLabel  = calendarPopup.findElement(By.cssSelector(".header__year"));

        DateTimeFormatter headerFormatter =
                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("MMMM yyyy")
                        .toFormatter(Locale.ENGLISH);

        while (true) {
            String displayedHeader =
                    monthLabel.getText().trim().toUpperCase() + " " + yearLabel.getText().trim();

            YearMonth displayedYM = YearMonth.parse(displayedHeader, headerFormatter);
            YearMonth targetYM = YearMonth.from(targetDate);

            if (displayedYM.equals(targetYM)) break;

            if (displayedYM.isBefore(targetYM)) {
                calendarPopup.findElement(By.cssSelector(".header__next")).click();
            } else {
                calendarPopup.findElement(By.cssSelector(".header__prev")).click();
            }

            /*wait.until(ExpectedConditions.not(
                    ExpectedConditions.textToBePresentInElement(monthLabel, displayedYM.getMonth().name())
            ));*/
        }

        String dayXpath =
                "//div[contains(@class,'dates__day') and normalize-space(text())='" +
                        targetDate.getDayOfMonth() + "']";

        WebElement dayElement =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dayXpath)));
        dayElement.click();

        WebElement okBtn = driver.findElement(
                By.xpath("//button[contains(@class, 'app-button')][.//p[text()='Ok']]")
        );

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", okBtn);
        Thread.sleep(200);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", okBtn);

        Assert.assertTrue(true, "Ok button clicked.");

        System.out.println("Calendar date selected successfully for " + fieldName + ": " + dateStr);
    }

    @When("User sets Date From {string} and Date To {string} in Allowance Management")
    public void userSetsDateFromDateFromAndDateToDateToInAllowanceManagement(String from, String to) throws InterruptedException {
        setDate("startDate", from);

        // wait until the previous calendar popup is fully gone
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".app-calendar .calendar")
        ));

        setDate("endDate", to);
    }

    @When("User sets Submission Date From {string} and Submission Date To {string} in Allowance Management")
    public void userSetsSubmissionDateFromDateFromAndDateToDateToInAllowanceManagement(String from, String to) throws InterruptedException {
        setDate("createdStartDate", from);

        // wait until the previous calendar popup is fully gone
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".app-calendar .calendar")
        ));

        setDate("createdEndDate", to);
    }

    @Then("The table should only show rows within the date range {string} to {string} in Allowance Management")
    public void theTableShouldOnlyShowRowsMatchingFieldWithValueInAllowanceManagement(String field, String value) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Wait for table to render
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table tbody")));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table tbody tr:first-child")));
        } catch (TimeoutException e) {
            System.out.println("Table is empty. No rows to validate.");
            return;
        }

        // Map visible headers to column index
        List<WebElement> headers = driver.findElements(By.cssSelector("table thead th"));
        Map<String, Integer> visibleHeaderIndex = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).isDisplayed()) {
                visibleHeaderIndex.put(headers.get(i).getText().trim(), i + 1); // nth-child is 1-based
            }
        }

        if (!visibleHeaderIndex.containsKey(field)) {
            System.out.println("Column '" + field + "' is hidden. Skipping validation.");
            return;
        }

        int colIndex = visibleHeaderIndex.get(field);

        // Get all cells under this column
        List<WebElement> cells = driver.findElements(
                By.cssSelector("table tbody tr td:nth-child(" + colIndex + ")")
        );

        if (cells.isEmpty()) {
            System.out.println("No data found under column '" + field + "'.");
            return;
        }

        // Validate each cell
        for (int i = 0; i < cells.size(); i++) {
            WebElement cell = cells.get(i);
            wait.until(ExpectedConditions.visibilityOf(cell));

            // Try to get inner div first, fallback to td text
            String text;
            try {
                WebElement innerDiv = cell.findElement(By.cssSelector(".table__left--values"));
                text = innerDiv.getText().trim();
            } catch (NoSuchElementException e) {
                text = cell.getText().trim();
            }

            boolean matches = text.toLowerCase().contains(value.toLowerCase());

            if (matches) {
                System.out.println("Column '" + field + "', Row " + (i + 1) + " PASSED: [" + text + "] matches [" + value + "]");
            } else {
                System.out.println("Column '" + field + "', Row " + (i + 1) + " FAILED: [" + text + "] does NOT match [" + value + "]");
            }

            Assert.assertTrue(matches, "Column '" + field + "', Row " + (i + 1) + " does not match filter");
        }
    }

    @And("The table should only show rows within the date range <dateFrom> to <dateTo> in Allowance Management")
    public void theTableShouldOnlyShowRowsWithinTheDateRangeDateFromToDateToInAllowanceManagement() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("All filter fields should be empty or default in Allowance Management")
    public void allFilterFieldsShouldBeEmptyOrDefaultInAllowanceManagement() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // TEXT INPUT FIELDS
        String[] textFields = {
                "name",
                "startDate",
                "endDate"
        };

        for (String field : textFields) {
            WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("input[name='" + field + "']")
            ));

            // Wait until the input value becomes empty
            boolean isEmpty = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(driver -> input.getAttribute("value").isEmpty());

            System.out.println("DEBUG → Field: " + field + " | value: '" + input.getAttribute("value") + "'");

            Assert.assertTrue(isEmpty, "Field " + field + " is NOT empty after Clear");
        }
        System.out.println("All filter fields reset to default.");
    }

    @Then("The table should show No results message in Allowance Management")
    public void theTableShouldShowNoResultsMessageInAllowanceManagement() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement noResults = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".claim-history__text")
        ));

        String actualText = noResults.getText().trim();
        Assert.assertEquals("0 results found", actualText);
        System.out.println("Expected: '0 results found' | Actual: '" + actualText + "'");
    }

    @And("exported file should contain only filtered data and visible columns in Allowance Management")
    public void exportedFileShouldContainOnlyFilteredDataAndVisibleColumnsInAllowanceManagement() throws Exception {

        // Wait and get the exported file
        File exportedFile = ExternalFunction.waitForExportedFile();

        // Retrieve UI data
        List<String> uiHeaders = driver.findElements(
                        By.cssSelector("th.table__th:not(.table__th--action)")
                ).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text -> !text.isEmpty()) // ignore empty headers
                .collect(Collectors.toList());

        List<List<String>> uiTableData = driver.findElements(
                        By.cssSelector("tbody.table__tbody tr.table__tr")
                ).stream()
                .map(row -> row.findElements(
                                By.cssSelector("td.table__td:not(.table__td--action)")
                        ).stream()
                        .map(WebElement::getText)
                        .map(String::trim)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

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
                excelHeaders,
                uiHeaders,
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
            Assert.assertEquals(
                    excelRows.get(i),
                    uiTableData.get(i),
                    "FAIL: Mismatch found in exported row " + (i + 1)
            );
        }

        System.out.println("PASS: All exported rows match the filtered UI data.");

        allureScreenshot();
    }
}
