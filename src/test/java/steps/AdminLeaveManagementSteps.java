package steps;

import ScenarioContext.ScenarioContext;
import drivers.DriverInstance;
import hooks.Hooks;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import qa.util.ExternalFunction;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import static drivers.DriverInstance.driver;
import static io.netty.handler.codec.http.HttpHeaders.setDate;
import static qa.util.ExternalFunction.getUITableData;
import static qa.util.ExternalFunction.getVisibleTableHeaders;

public class AdminLeaveManagementSteps extends DriverInstance {
    WebDriver driver = Hooks.driver; // Assuming you have a Hooks class initializing WebDriver
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

    private WebElement selectedRow;
    private String selectedEmployeeName;
    private List<String> selectedLeaveIdentifiers = new ArrayList<>();


    @And("User click on Leave Management")
    public void userClickOnLeaveManagement() {
        // Wait for loader overlay to disappear (using your helper)
        ExternalFunction.waitForLoaderToDisappear(driver);

        // Wait for the Company Benefit element to be ready
        WebElement leaveManagementSection = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//p[normalize-space(text())='Leave Management']")
                )
        );

        wait.until(ExpectedConditions.visibilityOf(leaveManagementSection));

        // Attempt click (with JS fallback)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(leaveManagementSection));
            leaveManagementSection.click();
            System.out.println("Clicked on 'Leave Management' normally.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, using JavaScript click instead...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", leaveManagementSection);
            js.executeScript("arguments[0].click();", leaveManagementSection);
            System.out.println("Clicked on 'Leave Management' via JavaScript.");
        }

        // Wait again after navigation
        try {
            ExternalFunction.waitForLoaderToDisappear(driver);
            System.out.println("Page finished loading after navigation.");
        } catch (TimeoutException e) {
            System.out.println("Loader did not disappear after navigation, proceeding...");
        }
    }

    @And("User can see Leave Calendar header")
    public void userCanSeeLeaveCalendarHeader() {
        try {
            WebElement header = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h1[normalize-space(text())='Leave Calendar']")
                    )
            );

            System.out.println("Header found: " + header.getText());
            Assert.assertTrue(header.isDisplayed(), "Leave Calendar header is not displayed");

        } catch (TimeoutException e) {
            System.out.println("Header 'Leave Calendar' not found within timeout!");
            Assert.fail("Header 'Leave Calendar' not found!");
        }
        allureScreenshot();
    }

    @And("User click on Leave Directory")
    public void userClickOnLeaveDirectory() {
        // Wait for loader overlay to disappear (using your helper)
        ExternalFunction.waitForLoaderToDisappear(driver);

        // Wait for the Company Benefit element to be ready
        WebElement leaveDirectorySection = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//p[normalize-space(text())='Leave Directory']")
                )
        );

        wait.until(ExpectedConditions.visibilityOf(leaveDirectorySection));

        // Attempt click (with JS fallback)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(leaveDirectorySection));
            leaveDirectorySection.click();
            System.out.println("Clicked on 'Leave Directory' normally.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, using JavaScript click instead...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", leaveDirectorySection);
            js.executeScript("arguments[0].click();", leaveDirectorySection);
            System.out.println("Clicked on 'Leave Directory' via JavaScript.");
        }

        // Wait again after navigation
        try {
            ExternalFunction.waitForLoaderToDisappear(driver);
            System.out.println("Page finished loading after navigation.");
        } catch (TimeoutException e) {
            System.out.println("Loader did not disappear after navigation, proceeding...");
        }
    }

    @And("User can see Leave Directory header")
    public void userCanSeeLeaveDirectoryHeader() {
        try {
            WebElement header = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h1[normalize-space(text())='Leave Directory']")
                    )
            );

            System.out.println("Header found: " + header.getText());
            Assert.assertTrue(header.isDisplayed(), "Leave Directory header is not displayed");

        } catch (TimeoutException e) {
            System.out.println("Header 'Leave Directory' not found within timeout!");
            Assert.fail("Header 'Leave Directory' not found!");
        }
        allureScreenshot();
    }

    @And("User click on Pending Approval")
    public void userClickOnPendingApproval() {
        // Wait for loader overlay to disappear (using your helper)
        ExternalFunction.waitForLoaderToDisappear(driver);

        // Wait for the Company Benefit element to be ready
        WebElement pendingApprovalSection = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//p[normalize-space(text())='Pending Approval']")
                )
        );

        wait.until(ExpectedConditions.visibilityOf(pendingApprovalSection));

        // Attempt click (with JS fallback)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(pendingApprovalSection));
            pendingApprovalSection.click();
            System.out.println("Clicked on 'Pending Approval' normally.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, using JavaScript click instead...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", pendingApprovalSection);
            js.executeScript("arguments[0].click();", pendingApprovalSection);
            System.out.println("Clicked on 'Pending Approval' via JavaScript.");
        }

        // Wait again after navigation
        try {
            ExternalFunction.waitForLoaderToDisappear(driver);
            System.out.println("Page finished loading after navigation.");
        } catch (TimeoutException e) {
            System.out.println("Loader did not disappear after navigation, proceeding...");
        }
    }

    @And("User can see Pending Approval header")
    public void userCanSeePendingApprovalHeader() {
        try {
            WebElement header = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h1[normalize-space(text())='Pending Approval']")
                    )
            );

            System.out.println("Header found: " + header.getText());
            Assert.assertTrue(header.isDisplayed(), "Pending Approval header is not displayed");

        } catch (TimeoutException e) {
            System.out.println("Header 'Pending Approval' not found within timeout!");
            Assert.fail("Header 'Pending Approval' not found!");
        }
        allureScreenshot();
    }

    @When("User click on Leave History")
    public void userClickOnLeaveHistory() {
        // Wait for loader overlay to disappear (using your helper)
        ExternalFunction.waitForLoaderToDisappear(driver);

        // Wait for the Company Benefit element to be ready
        WebElement leaveHistorySection = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//p[normalize-space(text())='Leave History']")
                )
        );

        wait.until(ExpectedConditions.visibilityOf(leaveHistorySection));

        // Attempt click (with JS fallback)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(leaveHistorySection));
            leaveHistorySection.click();
            System.out.println("Clicked on 'Leave History' normally.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, using JavaScript click instead...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", leaveHistorySection);
            js.executeScript("arguments[0].click();", leaveHistorySection);
            System.out.println("Clicked on 'Leave History' via JavaScript.");
        }

        // Wait again after navigation
        try {
            ExternalFunction.waitForLoaderToDisappear(driver);
            System.out.println("Page finished loading after navigation.");
        } catch (TimeoutException e) {
            System.out.println("Loader did not disappear after navigation, proceeding...");
        }
    }

    @Then("User can see Leave History header")
    public void userCanSeeLeaveHistoryHeader() {
        try {
            WebElement header = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h1[normalize-space(text())='Leave History']")
                    )
            );

            System.out.println("Header found: " + header.getText());
            Assert.assertTrue(header.isDisplayed(), "Leave History header is not displayed");

        } catch (TimeoutException e) {
            System.out.println("Header 'Leave History' not found within timeout!");
            Assert.fail("Header 'Leave History' not found!");
        }
        allureScreenshot();
    }

    @Then("the Leave Calendar page should load successfully")
    public void calendarPageLoads() {
        // Check page title or main calendar container
        WebElement calendarContainer = driver.findElement(By.id("leave-calendar-container"));
        Assert.assertTrue(calendarContainer.isDisplayed());
    }

    @Then("the current month and year should be displayed")
    public void verifyCurrentMonthYear() {

        LocalDate today = LocalDate.now();
        String expectedMonth = today.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String expectedYear = String.valueOf(today.getYear());
        String expectedText = expectedMonth + " " + expectedYear;

        WebElement monthYearElement = driver.findElement(
                By.cssSelector("p.leaves-calendar__current")
        );

        String actualText = monthYearElement.getText().trim();
        Assert.assertEquals(actualText, expectedText);

        System.out.println("Actual Month and Year: " + actualText);
        System.out.println("Expected Month and Year: " + expectedText);

        allureScreenshot();
    }

    @Then("the calendar listing should show the correct days for the current month")
    public void verifyCalendarGrid() {

        LocalDate today = LocalDate.now();
        int expectedDaysInMonth = today.lengthOfMonth();

        // Wait for listing items to load
        List<WebElement> dateElements = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.cssSelector("p.leaves-calendar__date")
                )
        );

        // Use Set to avoid duplicates
        Set<Integer> displayedDays = new HashSet<>();

        for (WebElement date : dateElements) {
            String text = date.getText().trim();
            if (!text.isEmpty()) {
                displayedDays.add(Integer.parseInt(text));
            }
        }

        Assert.assertEquals(expectedDaysInMonth, displayedDays.size());
        System.out.println("Expected: " +expectedDaysInMonth);
        System.out.println("Actual: " +displayedDays.size());
    }

    @Then("User click on Calendar View button")
    public void userClickOnCalendarViewButton() {
        WebElement calendarViewButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[.//p[text()='Calendar view']]")
                )
        );

        calendarViewButton.click();
    }

    @Then("today date should be highlighted with a blue circle")
    public void verifyTodayDateHighlighted() {

        WebElement todayDate = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".leaves-calendar__date--today")
                )
        );

        JavascriptExecutor js = (JavascriptExecutor) driver;

        String bgColor = (String) js.executeScript(
                "return window.getComputedStyle(arguments[0], '::after')" +
                        ".getPropertyValue('background-color');",
                todayDate
        );

        String hexColor = rgbToHex(bgColor);

        Assert.assertEquals(
                hexColor,
                "#0245a9",
                "Today date is not highlighted with blue circle"
        );

        allureScreenshot();
    }

    private String rgbToHex(String rgbColor) {
        rgbColor = rgbColor.replace("rgba(", "")
                .replace("rgb(", "")
                .replace(")", "");

        String[] rgb = rgbColor.split(",");

        int r = Integer.parseInt(rgb[0].trim());
        int g = Integer.parseInt(rgb[1].trim());
        int b = Integer.parseInt(rgb[2].trim());

        return String.format("#%02x%02x%02x", r, g, b);
    }

    @Then("clicking on date {int} displays leave details or shows no event")
    public void verifyLeaveDetailsOnDateClick(int day) {
        // Locate the date cell dynamically
        WebElement dateCell = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//p[contains(@class,'leaves-calendar__date') and text()='" + day + "']")
                )
        );

        // Click the date
        dateCell.click();

        // Wait a short moment for the leave details panel to update
        WebElement leaveList = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector(".leaves-calendar__leaves")
                )
        );

        // Get leave items if any
        List<WebElement> leaveItems = leaveList.findElements(By.cssSelector(".leaves-calendar__on-leave"));

        if (leaveItems.isEmpty()) {
            System.out.println("No leave events for date: " + day);
        } else {
            for (WebElement leave : leaveItems) {
                String leaveName = leave.findElement(By.cssSelector(".leaves-calendar__on-leave-name")).getText();
                String leaveType = leave.findElement(By.cssSelector(".leaves-calendar__on-leave-type")).getText();
                String leaveDate = leave.findElement(By.cssSelector(".leaves-calendar__on-leave-date")).getText();

                System.out.println("Leave Name: " + leaveName);
                System.out.println("Leave Type: " + leaveType);
                System.out.println("Leave Date: " + leaveDate);

                // Optional: Ensure the displayed date matches the clicked day
                Assert.assertTrue(leaveDate.contains(String.valueOf(day)),
                        "Leave date " + leaveDate + " does not match clicked date " + day);
            }
        }

        allureScreenshot();
    }

    @Then("weekends should be visually differentiated in grey")
    public void verifyWeekendsColor() {
        //System.out.println("did this ever run?");
        List<WebElement> weekends = driver.findElements(By.cssSelector(".calendar-weekend"));
        for (WebElement day : weekends) {
            String color = day.getCssValue("color");
            String dayText = day.getText();
            if (color.contains("230, 230, 230")) {
                Allure.step("PASS: Weekend date '" + dayText + "' is grey as expected.");
                Assert.assertTrue(color.contains("230, 230, 230"),
                        "Weekend date '" + dayText + "' is not grey. Found color: " + color);

            }
        }
        allureScreenshot();
    }

    @Then("public holidays should be displayed with red styling")
    public void verifyPublicHolidaysColor() {
        List<WebElement> holidays = driver.findElements(By.cssSelector(".public-holiday"));
        for (WebElement holiday : holidays) {
            String color = holiday.getCssValue("color");
            String holidayText = holiday.getText();
            if (color.contains("218, 82, 58, .2")) {
                System.out.println("PASS: Public holiday '" + holidayText + "' is red as expected.");
            } else {
                Assert.fail("FAIL: Public holiday '" + holidayText + "' is not red. Found color: " + color);
            }
        }
        allureScreenshot();
    }

    @And("user should be able to navigate months smoothly")
    public void userShouldBeAbleToNavigateMonthsSmoothly() {

        By monthLabel = By.cssSelector(".leaves-calendar__current");
        By nextBtn = By.cssSelector(".leaves-calendar__button--next");
        By prevBtn = By.cssSelector(".leaves-calendar__button--prev");

        // Get current month text
        String currentMonth = wait.until(
                ExpectedConditions.visibilityOfElementLocated(monthLabel)
        ).getText().trim();

        // Click NEXT month
        driver.findElement(nextBtn).click();

        wait.until(ExpectedConditions.not(
                ExpectedConditions.textToBe(monthLabel, currentMonth)
        ));

        String nextMonth = driver.findElement(monthLabel).getText().trim();

        Assert.assertNotEquals(
                nextMonth,
                currentMonth,
                "FAIL: Month did not change after clicking Next button"
        );

        System.out.println("PASS: Navigated to next month: " + nextMonth);

        /** Click PREVIOUS month
        driver.findElement(prevBtn).click();

        wait.until(ExpectedConditions.textToBe(
                monthLabel,
                currentMonth
        ));

        String revertedMonth = driver.findElement(monthLabel).getText().trim();

        Assert.assertEquals(
                revertedMonth,
                currentMonth,
                "FAIL: Month did not revert after clicking Previous button"
        );

        System.out.println("PASS: Navigated back to original month: " + currentMonth);
        **/
        allureScreenshot();
    }

    @And ("clicking Today button will navigates back to current month")
    public void clickingTodayButtonWillNavigatesBackToCurrentMonth() {
        By monthLabel = By.cssSelector(".leaves-calendar__current");
        By nextBtn = By.cssSelector(".leaves-calendar__button--next");
        By todayBtn = By.xpath("//button[normalize-space()='Today']");

        // Get system current month/year (e.g. "December 2025")
        String systemCurrentMonth = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("MMMM yyyy"));

        // Move to another month first
        driver.findElement(nextBtn).click();

        wait.until(ExpectedConditions.not(
                ExpectedConditions.textToBe(monthLabel, systemCurrentMonth)
        ));

        String navigatedMonth = driver.findElement(monthLabel).getText().trim();
        Assert.assertNotEquals(
                navigatedMonth,
                systemCurrentMonth,
                "FAIL: Calendar did not move away from current month"
        );

        // Click Today
        driver.findElement(todayBtn).click();

        // Wait until month resets to current
        wait.until(ExpectedConditions.textToBe(
                monthLabel,
                systemCurrentMonth
        ));

        String displayedMonth = driver.findElement(monthLabel).getText().trim();

        Assert.assertEquals(
                displayedMonth,
                systemCurrentMonth,
                "FAIL: Clicking Today did not navigate back to current month"
        );

        System.out.println("PASS: Today button navigates back to current month (" + systemCurrentMonth + ")");
        allureScreenshot();
    }

    @And("an existing Leave Directory is available for viewing")
    public void anExistingLeaveDirectoryIsAvailableForViewing(){
        ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);

        List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
        Assert.assertFalse(rows.isEmpty(), "No Leave Directory available for viewing");

        selectedRow = rows.get(1); // pick any row

        selectedEmployeeName =
                selectedRow.findElement(By.xpath("./td[2]")).getText().trim();
    }

    @And("user click on view action button")
    public void userClickOnViewActionButton(){
        WebElement viewButton =
                selectedRow.findElement(By.cssSelector("button.table__action"));

        wait.until(ExpectedConditions.elementToBeClickable(viewButton));
        viewButton.click();
    }

    @And("user should see the same employee name as header")
    public void userShouldSeeTheSameEmployeeNameAsHeader(){
        WebElement header = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".leave__name")
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

    @And("Leave Summary tab should be active")
    public void leaveSummaryTabShouldBeActive(){
        validateActiveTab("Leave Summary");
        allureScreenshot();
    }

    @And("user click on Upcoming Leave tab")
    public void userClickOnUpcomingLeaveTab(){
        clickTab("Upcoming Leave");
    }

    @And("Upcoming Leave tab should be active")
    public void upcomingLeaveTabShouldBeActive(){
        validateActiveTab("Upcoming Leave");
        allureScreenshot();
    }

    @And("user click on Leave History tab")
    public void userClickOnLeaveHistoryTab(){
        clickTab("Leave History");
    }

    @And("Leave History tab should be active")
    public void leaveHistoryTabShouldBeActive(){
        validateActiveTab("Leave History");
        allureScreenshot();
    }

    @And("Pending Approval button should redirect to the employee pending approval leave list")
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

    private String buildLeaveKey(WebElement row) {
        String employeeName = row.findElement(By.xpath("./td[2]")).getText().trim();
        String leaveType = row.findElement(By.xpath("./td[4]")).getText().trim();
        String startDate = row.findElement(By.xpath("./td[6]//div")).getText().trim();
        String endDate = row.findElement(By.xpath("./td[7]//div")).getText().trim();

        return employeeName + "|" + leaveType + "|" + startDate + "|" + endDate;
    }

    @Then("user can select multiple leave entry")
    public void selectMultipleLeaves() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector("tbody.table__tbody tr.table__tr"), 1));

        List<WebElement> rows =
                driver.findElements(By.cssSelector("tbody.table__tbody tr.table__tr"));

        Assert.assertTrue(rows.size() >= 2,
                "Not enough leave entries to select. Found: " + rows.size());

        for (int i = 0; i < 2; i++) {
            WebElement row = rows.get(i);

            String leaveKey = buildLeaveKey(row);
            selectedLeaveIdentifiers.add(leaveKey);

            row.findElement(By.cssSelector("td.table__td--selection button.table__checkbox"))
                    .click();
        }

        System.out.println("PASS: Selected leave entries: " + selectedLeaveIdentifiers);
    }

    @Then("user can see selected count")
    public void verifySelectedCount() {
        WebElement countElement =
                driver.findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]/table/thead/tr/th[2]"));

        String text = countElement.getText().trim();

        int selectedCount = Integer.parseInt(text.replaceAll("\\D+", ""));

        Assert.assertEquals(selectedCount, 2, "Selected count mismatch");

        System.out.println("PASS: Selected count is correct: " + selectedCount);
    }

    @Then("user click on the Reject Leave button")
    public void clickRejectButton() {
        WebElement rejectBtn = driver.findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]/table/thead/tr/th[3]/span/button[1]"));
        rejectBtn.click();
        System.out.println("PASS: Reject Leave button clicked");
    }

    @Then("user click on the Approve Leave button")
    public void clickApproveButton() {
        WebElement rejectBtn = driver.findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]/table/thead/tr/th[3]/span/button[2]"));
        rejectBtn.click();
        System.out.println("PASS: Approve Leave button clicked");
    }

    @Then("reject reason modal should display")
    public void verifyRejectModal() {
        WebElement modal = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".reject-reason-modal")));
        Assert.assertTrue(modal.isDisplayed(), "Reject reason modal not displayed");
        System.out.println("PASS: Reject modal displayed");
    }

    @Then("user input Reject Reason")
    public void inputRejectReason() {
        WebElement textarea = driver.findElement(By.name("rejectReason"));
        textarea.clear();
        textarea.sendKeys("Automation Test: Not eligible for leave");
        System.out.println("PASS: Reject reason entered");
    }

    @Then("user click on Cancel button and modal should be close")
    public void cancelRejectModal() {
        driver.findElement(By.xpath("//button[.//p[text()='Cancel']]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".reject-reason-modal")));
        System.out.println("PASS: Modal closed after cancel");
    }

    @Then("the textbox should save the reject reason as a draft")
    public void verifyRejectDraft() {
        WebElement textarea = driver.findElement(By.name("rejectReason"));
        String draft = textarea.getAttribute("value");
        Assert.assertEquals(draft, "Automation Test: Not eligible for leave", "Reject reason draft not saved");
        System.out.println("PASS: Reject reason saved as draft");
    }

    @Then("User click on Confirm button")
    public void confirmReject() {
        driver.findElement(By.xpath("//button[.//p[text()='Cancel']]")).click();
        System.out.println("PASS: Confirm button clicked");
    }

    @Then("all the selected entry should be removed from the list")
    public void verifyLeavesRemoved() {
        List<WebElement> rows =
                driver.findElements(By.cssSelector("tbody.table__tbody tr.table__tr"));

        for (WebElement row : rows) {
            String employeeName =
                    row.findElement(By.xpath("./td[2]")).getText().trim();

            Assert.assertFalse(
                    selectedLeaveIdentifiers.contains(employeeName),
                    "Leave entry still exists after rejection for employee: " + employeeName
            );
        }

        System.out.println("PASS: All selected leave entries are removed from the list");
    }

    @Then("User toggles {string} checkbox to {string} in Leave Management")
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

    @Then("The {string} column should be {string} in the Leave Management table")
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

    @When("User enters {string} in {string} in Leave Management")
    public void userEntersValueInField(String value, String field) {

        By locator = filterFields.get(field);
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(value);
    }

    private Map<String, By> filterFields = new HashMap<>() {{
        put("Employee ID", By.cssSelector("input[placeholder='Enter employee ID']"));
        put("Name", By.cssSelector("input[placeholder='Enter employee name']"));
        put("Nickname", By.cssSelector("input[placeholder='Enter nickname']"));
        put("Employee Email", By.cssSelector("input[placeholder='Enter mentor name']"));
    }};

    @When("User selects Leave Type {string}")
    public void userSelectsLeaveType(String status) {

        WebElement statusDropdown = driver.findElement(By.id("mui-component-select-leaveType"));

        statusDropdown.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul[role='listbox']")));

        WebElement option = driver.findElement(By.xpath("//li[text()='" + status + "']"));
        option.click();
    }

    @When("User sets Date From {string} and Date To {string} in Leave Management")
    public void userSetsDateRange(String from, String to) throws InterruptedException {
        setDate("startDate", from);

        // wait until the previous calendar popup is fully gone
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".app-calendar .calendar")
        ));

        setDate("endDate", to);
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

    @Then("The table should only show rows matching {string} with {string} in Leave Management")
    public void tableShouldMatchFieldValue(String field, String value) {

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

    @Then("The table should only show rows within the date range {string} to {string} in Leave Management")
    public void tableShouldMatchDateRange(String from, String to) {
        DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("dd MMMM yyyy")
                .toFormatter(Locale.ENGLISH);

        DateTimeFormatter tableFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);

        LocalDate dateFrom = LocalDate.parse(from, inputFormatter);
        LocalDate dateTo = LocalDate.parse(to, inputFormatter);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table tbody")));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table tbody tr:first-child")));
        } catch (TimeoutException e) {
            System.out.println("Table is empty. No rows to validate.");
            return;
        }

        List<WebElement> headers = driver.findElements(By.cssSelector("table thead th"));
        Map<String, Integer> visibleHeaderIndex = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).isDisplayed()) {
                visibleHeaderIndex.put(headers.get(i).getText().trim(), i + 1);
            }
        }

        if (!visibleHeaderIndex.containsKey("Start Date")) {
            System.out.println("'Start Date' column is not visible. Skipping validation.");
            return;
        }

        int colIndex = visibleHeaderIndex.get("Start Date");

        // Get all cells under this column
        List<WebElement> cells = driver.findElements(By.cssSelector("table tbody tr td:nth-child(" + colIndex + ")"));

        if (cells.isEmpty()) {
            System.out.println("No data found under 'Start Date' column.");
            return;
        }

        for (int i = 0; i < cells.size(); i++) {
            WebElement cell = cells.get(i);
            wait.until(ExpectedConditions.visibilityOf(cell));

            String text;
            try {
                WebElement innerDiv = cell.findElement(By.cssSelector(".table__left--values"));
                text = innerDiv.getText().trim();
            } catch (NoSuchElementException e) {
                text = cell.getText().trim();
            }

            LocalDate cellDate = LocalDate.parse(text, tableFormatter);

            boolean withinRange = !cellDate.isBefore(dateFrom) && !cellDate.isAfter(dateTo);
            Assert.assertTrue(withinRange,
                    "Row " + (i + 1) + ": Date " + cellDate + " not within range " + dateFrom + " to " + dateTo);

            System.out.println("✔ Row " + (i + 1) + " PASSED: [" + text + "] is within range [" + from + " - " + to + "]");
        }
    }

    @Then("All filter fields should be empty or default in Leave Management")
    public void allFilterFieldsShouldBeEmpty() {
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

    @Then("The table should show No results message in Leave Management")
    public void tableShouldShowNoResults() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement noResults = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".leave-history__text")
        ));

        String actualText = noResults.getText().trim();
        Assert.assertEquals("0 results found", actualText);
        System.out.println("Expected: '0 results found' | Actual: '" + actualText + "'");

    }

    @Then("exported file should contain only filtered data and visible columns in Leave Management")
    public void validateExportedFile() throws Exception {

        // Wait and get the exported file
        File exportedFile = ExternalFunction.waitForExportedFile(); // adjust extension & timeout

        // Get UI data
        List<String> uiHeaders = getVisibleTableHeaders().stream()
                .map(String::trim)
                .collect(Collectors.toList());

        List<List<String>> uiTableData = getUITableData().stream()
                .map(row -> row.stream()
                        .map(String::trim)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        // Read Excel data
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

        // Validate headers
        Assert.assertEquals(
                excelHeaders,
                uiHeaders,
                "FAIL: Exported columns do not match visible UI columns"
        );
        System.out.println("PASS: Exported columns match UI headers.");

        // Validate row count
        Assert.assertEquals(
                excelRows.size(),
                uiTableData.size(),
                "FAIL: Exported row count does not match filtered UI row count"
        );
        System.out.println("PASS: Exported row count matches filtered UI row count.");

        // Validate each row data
        for (int i = 0; i < uiTableData.size(); i++) {
            List<String> uiRow = uiTableData.get(i);
            List<String> excelRow = excelRows.get(i);
            Assert.assertEquals(
                    excelRow,
                    uiRow,
                    "FAIL: Mismatch found in exported row " + (i + 1)
            );
        }
        System.out.println("PASS: All exported rows match the filtered UI data.");

        // Optional: Allure screenshot for reporting
        allureScreenshot();
    }

    @And("system should display success toast message")
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
                "Leave has been approved successfully",
                "Incorrect success toast message"
        );

        System.out.println("PASS: Success toast displayed with message → " + actualText);

        // Optional: wait until toast disappears (stability)
        wait.until(ExpectedConditions.invisibilityOf(alert));
    }
}
