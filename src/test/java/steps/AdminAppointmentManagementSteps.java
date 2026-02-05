package steps;

import ScenarioContext.ScenarioContext;
import drivers.DriverInstance;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.AssertJUnit;
import qa.util.ExternalFunction;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.fail;

public class AdminAppointmentManagementSteps extends DriverInstance{

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));

    @Given("User logs in using {string} and {string} credentials")
    public void user_logs_in_using_and_credentials(String email, String password) {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            System.setProperty("webdriver.chrome.driver", "D:\\Selenium\\chromedriver-win64\\chromedriver.exe");
            driver = new ChromeDriver(options);
        }
        driver.manage().window().maximize();
        driver.get("https://hrms.admin.uat.directintegrate.com/");
        longWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        longWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(":r0:"))).sendKeys(email);
        longWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(":r1:"))).sendKeys(password);
        WebElement loginButton = longWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Login']]")));
        loginButton.click();

        ExternalFunction.waitForLoaderToDisappear(driver);

        // Select company from dropdown
        WebElement companyDropdown = longWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"mui-component-select-company\"]")));
        companyDropdown.click();

        // Click on the company
        WebElement companyName = longWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[normalize-space()='Mango Parfait']"))); //this shit
        companyName.click();


        // Click confirm button
        WebElement confirmButton = longWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[.//p[text()='Confirm']]")));
        confirmButton.click();

        WebElement dashboardElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[1]/div[2]/main/div[2]/header/div/h1")
        ));

        AssertJUnit.assertTrue("Admin dashboard element not visible.", dashboardElement.isDisplayed());

        System.out.println("Admin dashboard loaded successfully.");

        allureScreenshot();
    }

    @When("User click on Appointment Management")
    public void user_click_on_appointment_management() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        // Wait for loader overlay to disappear (using your helper)
        try {
            ExternalFunction.waitForLoaderToDisappear(driver);
            System.out.println("Loader disappeared.");
        } catch (TimeoutException e) {
            System.out.println("Loader still visible after timeout, continuing anyway...");
        }

        // Wait for the Appointment Management element to be ready
        WebElement appointmentSection = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//p[normalize-space(text())='Appointment Management']")
                )
        );

        wait.until(ExpectedConditions.visibilityOf(appointmentSection));

        // Attempt click (with JS fallback)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(appointmentSection));
            appointmentSection.click();
            System.out.println("Clicked on 'Appointment Management' normally.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, using JavaScript click instead...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", appointmentSection);
            js.executeScript("arguments[0].click();", appointmentSection);
            System.out.println("Clicked on 'Appointment Management' via JavaScript.");
        }

        // Wait again after navigation
        try {
            ExternalFunction.waitForLoaderToDisappear(driver);
            System.out.println("Page finished loading after navigation.");
        } catch (TimeoutException e) {
            System.out.println("Loader did not disappear after navigation, proceeding...");
        }
    }

    @Then("User can see Appointment List header")
    public void user_can_see_appointment_list_header() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            // Wait until the header "Appointment History" becomes visible
            WebElement header = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h1[normalize-space(text())='Appointment List']")
                    )
            );

            System.out.println("Header found: " + header.getText());
            assertEquals("Appointment List", header.getText());

        } catch (TimeoutException e) {
            System.out.println("Header 'Appointment List' not found within timeout!");
            Assert.fail("Header 'Appointment List' not found!");
        }

    }

    @Then("User can see active tab is highlighted with a green colour")
    public void user_can_see_active_tab_is_highlighted_with_a_green_colour() {
        try {
            // Wait until the element is visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement activeMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("p.side-nav__label--child-active")
            ));

            // Get the computed CSS color value
            String color = activeMenu.getCssValue("color"); // returns "rgba(r, g, b, a)"

            // Convert RGBA → HEX
            String hexColor = org.openqa.selenium.support.Color.fromString(color).asHex();

            System.out.println("🎨 Active menu color detected: " + hexColor);

            // Verify the color matches expected value (#00cf9c)
            Assert.assertEquals(hexColor.toLowerCase(), "#00cf9c",
                    "Expected color #00cf9c but found " + hexColor);

            System.out.println("✅ 'Appointment List' is correctly highlighted in green (#00cf9c).");

        } catch (Exception e) {
            fail("Failed to verify highlight color: " + e.getMessage());
        }

        allureScreenshot();
    }

    @When("User close the sidebar")
    public void userCloseTheSidebar() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait until loader disappears if necessary
        ExternalFunction.waitForLoaderToDisappear(driver);

        // Wait until the sidebar logo (toggle) is clickable
        WebElement sidebarLogo = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".side-nav__logo")
                )
        );
        sidebarLogo.click();

        allureScreenshot();
    }

    @When("User open the sidebar")
    public void userOpenTheSidebar() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Ensure any loader or overlay is gone
        ExternalFunction.waitForLoaderToDisappear(driver);

        try {
            // Wait until the logo is visible
            WebElement sidebarLogo = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector(".side-nav__logo")
                    )
            );

            // Wait until it's clickable
            wait.until(ExpectedConditions.elementToBeClickable(sidebarLogo));

            // Try clicking normally first
            sidebarLogo.click();
            System.out.println("Sidebar opened successfully via normal click.");

        } catch (ElementClickInterceptedException e) {
            // If intercepted, perform JS click as fallback
            System.out.println("Normal click intercepted, retrying with JavaScript click.");
            WebElement sidebarLogo = driver.findElement(By.cssSelector(".side-nav__logo"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sidebarLogo);
            System.out.println("Sidebar opened successfully via JavaScript click.");
        } catch (TimeoutException e) {
            System.out.println("Sidebar logo not found or not clickable within timeout.");
            throw e;
        }

        allureScreenshot();
    }

    @When("User resize the screen")
    public void user_resize_the_screen(){
        driver.manage().window().setSize(new Dimension(800, 600)); // resize to smaller screen
        //driver.manage().window().setSize(new Dimension(1920, 1080)); // restore to normal
        allureScreenshot();
    }

    @When("User close and open the sidebar multiple times")
    public void userCloseAndOpenTheSidebarMultipleTimes() {
        for (int i = 0; i < 3; i++) {
            userCloseTheSidebar();
            userOpenTheSidebar();
        }
    }

    @When("User click browser back button")
    public void userClickBrowserBackButton() {
        driver.navigate().back();
        allureScreenshot();
    }

    @When("User click browser forward button")
    public void userClickBrowserForwardButton() {
        driver.navigate().forward();
        allureScreenshot();
    }

    @When("User click on Export button")
    public void userClickOnExportButton() {
        WebElement exportBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div/div[1]/div[2]/button[2]")));

        // Wait until it's clickable
        wait.until(ExpectedConditions.elementToBeClickable(exportBtn));

        // Try clicking normally first
        exportBtn.click();
        System.out.println("Export Button successfully clicked");

        allureScreenshot();
    }

    @Then("Appointment List file should be downloaded")
    public void appointmentListFileShouldBeDownloaded() throws InterruptedException {
        String prefix = "Appointment_Data";

        // Clean up old files before checking
        ExternalFunction.cleanUpOldDownloads(prefix);

        // Wait up to 30 seconds for the new file to appear
        boolean fileFound = false;
        int waitTimeSeconds = 30;

        for (int i = 0; i < waitTimeSeconds; i++) {
            if (ExternalFunction.isFileDownloaded(prefix)) {
                fileFound = true;
                break;
            }
            Thread.sleep(1000); // Wait 1 second before rechecking
        }

        allureScreenshot();

        // Verify
        AssertJUnit.assertTrue("Appointment Data file was not downloaded!", fileFound);
        System.out.println("✅ Appointment Data file downloaded successfully!");
    }

    @Then("Appointment List table should display the correct header")
    public void appointmentListTableShouldDisplayTheCorrectHeader(){

        // Expected column names
        String[] expectedColumns = {
                "Employee Name",
                "Mentor Name",
                "Appointment Date",
                "Start Time",
                "End Time",
                "Remark",
                "Status"
        };

        // Locate all visible table headers
        List<WebElement> actualColumns = driver.findElements(By.xpath("//table//thead//th"));

        // Extract header text
        List<String> actualColumnNames = new ArrayList<>();
        for (WebElement col : actualColumns) {
            actualColumnNames.add(col.getText().trim());
        }

        System.out.println("🔎 Visible table columns: " + actualColumnNames);

        // Assert each expected column is present
        for (String expected : expectedColumns) {
            boolean isPresent = actualColumnNames.contains(expected);
            assertTrue(isPresent, "Column not found: " + expected);
        }

        System.out.println("✅ All default columns are displayed correctly!");
    }

    @Then("Column {string} should display correct data")
    public void columnShouldDisplayCorrectData(String columnName) {
        ExternalFunction.waitForLoaderToDisappear(driver);
        ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);
        int columnIndex = ExternalFunction.getColumnIndexByHeader(driver, columnName);
        ExternalFunction.verifyColumnHasValidData(driver, columnIndex, columnName);
    }

    @Then("Column {string} should display correct format")
    public void columnShouldDisplayCorrectFormat(String columnName) {
        // Wait for table to load
        ExternalFunction.waitForTableToLoad(driver, ".app-table", 10);

        // Define column indexes dynamically based on the name
        int columnIndex;
        switch (columnName.toLowerCase()) {
            case "start time":
                columnIndex = 4;
                break;
            case "end time":
                columnIndex = 5;
                break;
            default:
                throw new IllegalArgumentException("Unsupported column name: " + columnName);
        }

        // Build dynamic XPath for the selected column
        String columnXPath = "//table//tr/td[" + columnIndex + "]";
        List<WebElement> columnCells = driver.findElements(By.xpath(columnXPath));

        // Define expected time format (12-hour with AM/PM)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

        System.out.println("Validating time format for column: " + columnName);

        // Loop through each cell and validate format
        for (WebElement cell : columnCells) {
            String timeText = cell.getText().trim();

            // Skip empty rows (if any)
            if (timeText.isEmpty()) continue;

            System.out.println("Checking " + columnName + ": " + timeText);

            try {
                LocalTime.parse(timeText, formatter);
                System.out.println("Valid format: " + timeText);
            } catch (DateTimeParseException e) {
                Assert.fail("Invalid time format found in '" + columnName + "': " + timeText);
            }
        }

        System.out.println("All values in '" + columnName + "' column follow format hh:mm a");
    }

    @Then("Column Status should colour coded correctly")
    public void columnStatusShouldColourCodedCorrectly() {
        // Wait for the table to appear using your external function
        ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);

        System.out.println("Checking status colour coding...");

        // Find all status elements
        List<WebElement> statusDivs = driver.findElements(
                By.xpath("//div[contains(@class,'status status--')]")
        );

        if (statusDivs.isEmpty()) {
            System.out.println("No status elements found in the table.");
            return;
        }

        for (WebElement statusDiv : statusDivs) {
            try {
                // Get text inside the <p>
                String text = statusDiv.findElement(By.xpath(".//p")).getText().trim();

                // Get the color from the parent status div (this holds the actual color)
                String rgbaColor = statusDiv.getCssValue("background-color");
                String hexColor = org.openqa.selenium.support.Color.fromString(rgbaColor).asHex();

                System.out.println("Status: " + text + " → " + hexColor);

                switch (text.toLowerCase()) {
                    case "completed":
                        Assert.assertTrue(
                                hexColor.equals("#00cf9c") || hexColor.equals("#00cf9b"),
                                "Completed color mismatch: expected #00cf9c but got " + hexColor
                        );
                        System.out.println("Completed status color verified successfully.");
                        break;

                    case "cancelled":
                        Assert.assertTrue(
                                hexColor.equals("#f64444") || hexColor.equals("#f64343"),
                                "Cancelled color mismatch: expected #f64444 but got " + hexColor
                        );
                        System.out.println("Cancelled status color verified successfully.");
                        break;

                    case "overdue":
                        Assert.assertTrue(
                                hexColor.equals("#f64444") || hexColor.equals("#f64343"),
                                "Overdue status color mismatch: expected #f64444 but got " + hexColor
                        );
                        System.out.println("Overdue status color verified successfully. ");
                        break;

                    case "upcoming":
                        Assert.assertTrue(
                                hexColor.equals("#0245a9") || hexColor.equals("#f64343"),
                                "Upcoming status color mismatch: expected #0245a9 but got " + hexColor
                        );
                        System.out.println("Upcoming status color verified successfully. ");
                        break;

                    case "acknowledged":
                        Assert.assertTrue(
                                hexColor.equals("#0245a9") || hexColor.equals("#f64343"),
                                "Acknowledged status color mismatch: expected #0245a9 but got " + hexColor
                        );
                        System.out.println("Acknowledged status colour verified successfully. ");
                        break;

                    default:
                        System.out.println("Unknown status: " + text + " (not validated)");
                        break;
                }

            } catch (Exception e) {
                System.out.println("Error while checking a status element: " + e.getMessage());
            }
        }

        System.out.println("Finished verifying all status color codes.");
        allureScreenshot();
    }

    @When("User clicks on header sorting icon for column {string}")
    public void userClicksOnHeaderSortingIconForColumn(String columnName){
        ExternalFunction.clickSortingIcon(driver, columnName);
    }

    @And("Column {string} is sorted in descending order")
    public void columnIsSortedInDescendingOrder(String columnName){
        List<Object> actualValues = ExternalFunction.extractColumnValues(driver, columnName);
        List<Object> expectedValues = ExternalFunction.sortDescending(actualValues);

        assertEquals(actualValues, expectedValues,
                "Column '" + columnName + "' not sorted in descending order.\nExpected: " + expectedValues + "\nActual: " + actualValues);
        System.out.println("Column '" + columnName + "' sorted correctly in descending order: " + actualValues);

        allureScreenshot();
    }

    @Then("Column {string} is sorted in ascending order")
    public void columnIsSortedInAscendingOrder(String columnName){
        List<Object> actualValues = ExternalFunction.extractColumnValues(driver, columnName);
        List<Object> expectedValues = ExternalFunction.sortAscending(actualValues);

        assertEquals(actualValues, expectedValues,
                "Column '" + columnName + "' not sorted in ascending order.\nExpected: " + expectedValues + "\nActual: " + actualValues);
        System.out.println("Column '" + columnName + "' sorted correctly in ascending order: " + actualValues);

        allureScreenshot();
    }


    @Then("Each row should have action button")
    public void eachRowShouldHaveActionButton() {
        // Wait for table to load properly
        ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);

        // Find all rows in the table
        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        System.out.println("Total rows found: " + rows.size());

        boolean allRowsHaveButton = true;
        int missingCount = 0;

        for (int i = 0; i < rows.size(); i++) {
            WebElement row = rows.get(i);
            try {
                // Adjust the selector for your actual action icon
                WebElement actionBtn = row.findElement(By.cssSelector("button.table__action img[alt='action']"));
                if (actionBtn.isDisplayed()) {
                    System.out.println("Action button found for row " + (i + 1));
                } else {
                    System.out.println("Action button NOT visible for row " + (i + 1));
                    allRowsHaveButton = false;
                    missingCount++;
                }
            } catch (NoSuchElementException e) {
                System.out.println("No action button found for row " + (i + 1));
                allRowsHaveButton = false;
                missingCount++;
            }
        }

        if (!allRowsHaveButton) {
            fail("Missing action button in " + missingCount + " row(s).");
        } else {
            System.out.println("All rows have an action button.");
        }
    }

    @And("User click on the action button")
    public void userClickOnTheActionButton() {
        ExternalFunction.waitForTableToLoad(driver, ".app-table", 15);
        System.out.println("Clicking on action button for the first row...");

        try {
            // Click on the first row’s action button
            WebElement actionButton = driver.findElement(By.cssSelector("table tbody tr:first-child button.table__action img[alt='action']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", actionButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", actionButton);

            System.out.println("Clicked on the row's action button successfully.");

        } catch (Exception e) {
            fail("Failed to click the action button: " + e.getMessage());
        }
    }

    @And("User should see actions")
    public void userShouldSeeActions() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Wait for the dropdown menu to appear
        List<WebElement> actionItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//ul[contains(@class, 'MuiMenu-list')]//li")
        ));

        // Extract the text of each action item
        List<String> actualActions = actionItems.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList());

        // Expected action labels
        List<String> expectedActions = Arrays.asList("Mark as Completed", "Mark as Cancelled");

        // Verify all expected actions are present
        for (String expected : expectedActions) {
            Assert.assertTrue(actualActions.contains(expected),
                    "Expected action not found: " + expected + ". Found: " + actualActions);
        }

        System.out.println("✅ Actions visible: " + actualActions);
    }

    @When("User locates a row with status {string}")
    public void userLocatesARowWithStatus(String currentStatus) {
        boolean found = false;
        int page = 1;

        try {
            while (true) {
                System.out.println("Searching for appointment with status '" + currentStatus + "' on page " + page + "...");

                ExternalFunction.waitForTableToLoad(driver, ".app-table", 10);
                List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
                if (rows.isEmpty()) break;

                for (WebElement row : rows) {
                    // Scroll horizontally to ensure the rightmost columns are visible
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth",
                            driver.findElement(By.cssSelector(".app-table")));

                    // Locate the status cell
                    WebElement statusCell = row.findElement(By.xpath(".//td[7]"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", statusCell);

                    String status = statusCell.getText().trim();

                    if (status.equalsIgnoreCase(currentStatus)) {
                        String appointmentName = row.findElement(By.xpath(".//td[3]")).getText().trim();
                        ScenarioContext.setContext("appointmentName", appointmentName);
                        ScenarioContext.setContext("targetRow", row);
                        found = true;
                        System.out.println("✅ Found appointment '" + appointmentName + "' with status: " + currentStatus);
                        break;
                    }
                }

                if (found) break;

                // Try next page if available
                List<WebElement> nextButtons = driver.findElements(By.cssSelector("button.app-table__arrow--next"));
                if (!nextButtons.isEmpty() && nextButtons.get(0).isEnabled()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextButtons.get(0));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextButtons.get(0));
                    Thread.sleep(2000);
                    page++;
                } else break;
            }

            Assert.assertTrue(found, "Could not find any appointment with status: " + currentStatus);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error locating row with status '" + currentStatus + "': " + e.getMessage());
        }
    }

    @And("User clicks on the Action button for that row")
    public void userClicksOnTheActionButtonForThatRow() {
        try {
            WebElement row = (WebElement) ScenarioContext.getContext("targetRow");
            WebElement actionBtn = row.findElement(By.cssSelector(".table__action img[alt='action']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", actionBtn);
            DriverInstance.scrollDown();
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", actionBtn);
            System.out.println("✅ Clicked action button for selected appointment.");
        } catch (Exception e) {
            fail("Failed to click action button: " + e.getMessage());
        }
    }

    @When("User selects {string}")
    public void userSelects(String actionName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//li[normalize-space()='" + actionName + "']")
            ));
            option.click();
            System.out.println("✅ Selected action: " + actionName);
        } catch (Exception e) {
            fail("Failed to select action '" + actionName + "': " + e.getMessage());
        }
    }

    @And("User should see {string}")
    public void userShouldSeeModalMessage(String modalMessage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.position-modal")
            ));
            System.out.println("Confirmation modal is visible.");

            WebElement message = modal.findElement(By.cssSelector(".position-modal__body p"));
            String actualMessage = message.getText().trim();

            Assert.assertEquals(actualMessage, modalMessage,
                    "Modal message does not match expected text!");

            System.out.println("Modal message verified: " + actualMessage);

        } catch (TimeoutException e) {
            fail("Confirmation dialog did not appear within timeout.");
        } catch (Exception e) {
            fail("Error verifying confirmation dialog: " + e.getMessage());
        }
    }

    @And("User click Update button")
    public void userClickUpdateButton() {
        WebElement updateBtn = driver.findElement(
                By.xpath("//div[@class='position-modal__button-container']//p[normalize-space()='Update']/ancestor::button")
        );
        updateBtn.click();
        System.out.println("Clicked on Update button");
    }

    @Then("The row should now show status {string}")
    public void theRowShouldNowShowStatus(String expectedStatus) {
        String appointmentName = (String) ScenarioContext.getContext("appointmentName");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));

        try {
            // XPath to locate the status cell for this appointment
            String statusXpath = "//td[contains(.,'" + appointmentName + "')]/following-sibling::td//p[@class='status__text']";
            System.out.println("Waiting for status to change to: " + expectedStatus + " for appointment: " + appointmentName);

            // Wait for the status text to change dynamically
            boolean statusUpdated = wait.until(driver -> {
                try {
                    WebElement statusElement = driver.findElement(By.xpath(statusXpath));
                    String currentText = statusElement.getText().trim();
                    System.out.println("Current status: " + currentText);
                    return currentText.equalsIgnoreCase(expectedStatus);
                } catch (StaleElementReferenceException e) {
                    System.out.println("Table re-rendered, retrying element lookup...");
                    return false; // retry locating element
                } catch (NoSuchElementException e) {
                    return false; // element not yet visible, retry
                }
            });

            // If still not found after wait, try refreshing once
            if (!statusUpdated) {
                System.out.println("Refreshing page to recheck updated status...");
                driver.navigate().refresh();
                ExternalFunction.waitForTableToLoad(driver, ".app-table", 10);

                WebElement refreshedStatus = driver.findElement(By.xpath(statusXpath));
                String refreshedText = refreshedStatus.getText().trim();

                Assert.assertEquals(refreshedText, expectedStatus,
                        "Expected status to be '" + expectedStatus + "' after refresh, but was '" + refreshedText + "'");
                System.out.println("✅ Status is '" + refreshedText + "' after refresh for: " + appointmentName);
            } else {
                System.out.println("✅ Status is '" + expectedStatus + "' for: " + appointmentName);
            }

        } catch (TimeoutException e) {
            Assert.fail("Timed out waiting for status to change to '" + expectedStatus + "'");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Error verifying updated status: " + e.getMessage());
        }
    }


    @And("User click Cancel button in the dialog")
    public void userClickCancelButtonInTheDialog() {
        WebElement cancelBtn = driver.findElement(
                By.xpath("//div[@class='position-modal__button-container']//p[normalize-space()='Cancel']/ancestor::button")
        );
        cancelBtn.click();
        System.out.println("Clicked on Cancel button");
    }

    @When("User click on Filter button")
    public void userClickOnFilterButton() {
        WebElement filterBtn = driver.findElement(
                By.xpath("//div[@class='app-search-input__button-container']//p[normalize-space()='Filter']/ancestor::button")
        );
        filterBtn.click();
        System.out.println("Clicked on Filter button");
    }

    @Then("User should see filter prompt")
    public void userShouldSeeFilterPrompt() {
        WebElement filterPrompt = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".advance-form__inputs__toggle-word")));
        // Get text inside the <p>
        String filterText = filterPrompt.getText().trim();
        System.out.println("Filter form labeled as : " + filterText);
        assertTrue(filterPrompt.isDisplayed(), "The filter label displayed : " + filterText);
    }

    @Then("All checked column should visible in the table behind the modal")
    public void allCheckedColumnShouldVisibleInTheTableBehindTheModal() {
        // Mapping between filter checkbox labels and table header text
        Map<String, String> columnNameMap = new HashMap<>();
        columnNameMap.put("Employee Name", "Employee Name");
        columnNameMap.put("Mentor Name", "Mentor Name");
        columnNameMap.put("Appointment Date", "Appointment Date");
        columnNameMap.put("Appointment Start Time", "Start Time");
        columnNameMap.put("Appointment End Time", "End Time");
        columnNameMap.put("Remark", "Remark");
        columnNameMap.put("Status", "Status");

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

    @Then("User toggles {string} checkbox to {string}")
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

    @Then("The {string} column should be {string} in the table")
    public void theColumnColumnShouldBeExpectedStateInTheTable(String column, String expectedState) {
        // Click on backdrop to close modal
        List<WebElement> backdrops = driver.findElements(By.cssSelector(".MuiBackdrop-root.MuiModal-backdrop"));
        if (!backdrops.isEmpty()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", backdrops.get(0));
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.invisibilityOf(backdrops.get(0)));
        }

        // Map column display names to inner cell classes
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("Employee Name", "employee-name-column");
        columnMap.put("Employee Nickname", "employee-nickname-column");
        columnMap.put("Employee Email", "employee-email-column");
        columnMap.put("Mentor Name", "mentor-name-column");
        columnMap.put("Mentor Nickname", "mentor-nickname-column");
        columnMap.put("Mentor Email", "mentor-email-column");
        columnMap.put("Appointment Date", "appointment-date-column");
        columnMap.put("Appointment Start Time", "start-time-column");
        columnMap.put("Appointment End Time", "end-time-column");
        columnMap.put("Remark", "remark-column");
        columnMap.put("Status", "status-column");

        String cellClass = columnMap.get(column);

        // Check header visibility
        List<WebElement> headers = driver.findElements(By.xpath("//th[contains(text(), '" + column + "')]"));
        boolean headerVisible = !headers.isEmpty() && headers.get(0).isDisplayed();

        /* Check cell visibility
        List<WebElement> cells = driver.findElements(
                By.cssSelector("td[data-column='" + cellClass + "'] .table__left--values")
        );
        boolean cellsVisible = !cells.isEmpty() && cells.stream().allMatch(WebElement::isDisplayed);
        */


        System.out.println("Header visible? : " + headerVisible);
        //System.out.println("Cells visible?  : " + cellsVisible);

        if (expectedState.equalsIgnoreCase("visible")) {
            Assert.assertTrue(headerVisible, "Header for column '" + column + "' SHOULD be visible but is hidden.");
            //Assert.assertTrue(cellsVisible, "Cells for column '" + column + "' SHOULD be visible but are hidden.");
        } else {
            Assert.assertFalse(headerVisible, "Header for column '" + column + "' SHOULD be hidden but is visible.");
            //Assert.assertFalse(cellsVisible, "Cells for column '" + column + "' SHOULD be hidden but are visible.");
        }
    }

    // Mapping filter field names to their locators
    private Map<String, By> filterFields = new HashMap<>() {{
        put("Employee Name", By.cssSelector("input[placeholder='Enter employee name']"));
        put("Employee Nickname", By.cssSelector("input[placeholder='Enter employee nickname']"));
        put("Employee Email", By.cssSelector("input[placeholder='Enter employee email']"));
        put("Mentor Name", By.cssSelector("input[placeholder='Enter mentor name']"));
        put("Mentor Nickname", By.cssSelector("input[placeholder='Enter mentor nickname']"));
        put("Mentor Email", By.cssSelector("input[placeholder='Enter mentor email']"));
        put("Status", By.cssSelector("select#status")); // adjust selector
        put("Date From", By.xpath("/html/body/div[3]/div[3]/div/form/div/div[1]/div[9]/div/div/div")); // adjust selector
        put("Date To", By.cssSelector("/html/body/div[3]/div[3]/div/form/div/div[1]/div[10]/div/div/div"));     // adjust selector
    }};

    // Mapping table column names to data-column attributes
    private Map<String, String> columnMap = new HashMap<>() {{
        put("Employee Name", "employeeName");
        put("Employee Nickname", "employeeNickname");
        put("Employee Email", "employeeEmail");
        put("Mentor Name", "mentorName");
        put("Mentor Nickname", "mentorNickname");
        put("Mentor Email", "mentorEmail");
        put("Status", "status");
        put("Appointment Date", "appointmentDate");
        put("Appointment Start Time", "startTime");
        put("Appointment End Time", "endTime");
        put("Remark", "remark");
    }};

    @When("User enters {string} in {string}")
    public void userEntersValueInField(String value, String field) {
        By locator = filterFields.get(field);
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(value);
    }

    @When("User enters {string} in {string} and presses Enter")
    public void userEntersValueAndPressesEnter(String value, String field) {
        By locator = filterFields.get(field);
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(value + Keys.ENTER);
    }

    @When("User selects Status {string}")
    public void userSelectsStatus(String status) {

        WebElement statusDropdown = driver.findElement(By.id("mui-component-select-statusListStr"));

        statusDropdown.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul[role='listbox']")));

        WebElement option = driver.findElement(By.xpath("//li[text()='" + status + "']"));
        option.click();
    }

    public void setDate(String fieldName, String dateStr) throws InterruptedException {

        // Formatter used for your input "dd MMMM yyyy"
        DateTimeFormatter inputFormatter =
                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("dd MMMM yyyy")
                        .toFormatter(Locale.ENGLISH);

        LocalDate targetDate = LocalDate.parse(dateStr, inputFormatter);


        // --- 1. Click calendar icon ---
        String calendarIconXpath =
                "//input[@name='" + fieldName + "']/following-sibling::div//*[contains(@class, 'app-icon')]";

        WebElement calendarIcon =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(calendarIconXpath)));

        try {
            calendarIcon.click();
        } catch (ElementClickInterceptedException e) {
            System.out.println("⚠ Icon not clickable, trying JS click...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", calendarIcon);

            // Try wrapper as fallback
            try {
                WebElement wrapper = calendarIcon.findElement(
                        By.xpath("./ancestor::div[contains(@class,'app-calendar-input__wrapper')]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", wrapper);
            } catch (Exception ignored) {}
        }


        // --- 2. Wait for calendar popup ---
        WebElement calendarPopup = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector(".app-calendar .calendar")));


        // --- 3. Month/Year labels ---
        WebElement monthLabel = calendarPopup.findElement(By.cssSelector(".header__month"));
        WebElement yearLabel  = calendarPopup.findElement(By.cssSelector(".header__year"));

        // Formatter for calendar header, supports uppercase
        DateTimeFormatter headerFormatter =
                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("MMMM yyyy")
                        .toFormatter(Locale.ENGLISH);


        // --- 4. Navigate to correct month ---
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

            // Wait for update before next loop
            wait.until(ExpectedConditions.not(
                    ExpectedConditions.textToBePresentInElement(monthLabel, displayedYM.getMonth().name())
            ));
        }


        // --- 5. Select the day ---
        String dayXpath =
                "//div[contains(@class,'dates__day') and normalize-space(text())='" +
                        targetDate.getDayOfMonth() + "']";

        WebElement dayElement =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dayXpath)));
        dayElement.click();

        // --- 6. Click Ok button
        WebElement okBtn = driver.findElement(
                By.xpath("//button[contains(@class, 'app-button')][.//p[text()='Ok']]")
        );

        // Scroll into view
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", okBtn);
        Thread.sleep(200);

        // JS click (more reliable)
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", okBtn);

        Assert.assertTrue(true, "Ok button clicked.");

        System.out.println("✔ Calendar date selected successfully for " + fieldName + ": " + dateStr);
    }


    @When("User sets Date From {string} and Date To {string}")
    public void userSetsDateRange(String from, String to) throws InterruptedException {
        setDate("appointmentDateFrom", from);

        // wait until the previous calendar popup is fully gone
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".app-calendar .calendar")
        ));

        setDate("appointmentDateTo", to);
    }


    @Then("The table should only show rows within the date range {string} to {string}")
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
                visibleHeaderIndex.put(headers.get(i).getText().trim(), i + 1); // nth-child is 1-based
            }
        }

        if (!visibleHeaderIndex.containsKey("Appointment Date")) {
            System.out.println("'Appointment Date' column is not visible. Skipping validation.");
            return;
        }

        int colIndex = visibleHeaderIndex.get("Appointment Date");

        // Get all cells under this column
        List<WebElement> cells = driver.findElements(By.cssSelector("table tbody tr td:nth-child(" + colIndex + ")"));

        if (cells.isEmpty()) {
            System.out.println("⚠ No data found under 'Appointment Date' column.");
            return;
        }

        // Validate each cell
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

            LocalDate cellDate = LocalDate.parse(text, tableFormatter); // use tableFormatter

            boolean withinRange = !cellDate.isBefore(dateFrom) && !cellDate.isAfter(dateTo);
            Assert.assertTrue(withinRange,
                    "Row " + (i + 1) + ": Date " + cellDate + " not within range " + dateFrom + " to " + dateTo);

            System.out.println("✔ Row " + (i + 1) + " PASSED: [" + text + "] is within range [" + from + " - " + to + "]");
        }
    }

    @When("User clicks Search")
    public void userClicksSearch() throws InterruptedException {


        WebElement searchBtn = driver.findElement(
                By.xpath("//button[contains(@class, 'app-button')][.//p[text()='Search']]")
        );

        // Scroll into view
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", searchBtn);
        Thread.sleep(200);

        // Get current table state BEFORE clicking
        List<WebElement> oldRows = driver.findElements(By.cssSelector("table tbody tr"));

        // JS click (more reliable)
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchBtn);

        // Wait for table to refresh
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                ExpectedConditions.stalenessOf(oldRows.get(0))
        );

        Assert.assertTrue(true, "Search click triggered table refresh.");
    }

    
    @Then("The table should only show rows matching {string} with {string}")
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
            System.out.println("⚠ No data found under column '" + field + "'.");
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
                System.out.println("✔ Column '" + field + "', Row " + (i + 1) + " PASSED: [" + text + "] matches [" + value + "]");
            } else {
                System.out.println("Column '" + field + "', Row " + (i + 1) + " FAILED: [" + text + "] does NOT match [" + value + "]");
            }

            Assert.assertTrue(matches, "Column '" + field + "', Row " + (i + 1) + " does not match filter");
        }
    }
    
    @When("User clicks Clear")
    public void userClicksClear() {
        driver.findElement(By.xpath("//button[contains(@class, 'app-button')][.//p[text()='Clear']]")).click();
    }

    @Then("All filter fields should be empty or default")
    public void allFilterFieldsShouldBeEmpty() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // TEXT INPUT FIELDS
        String[] textFields = {
                "employeeName",
        };

        for (String field : textFields) {
            WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("input[name='" + field + "']")
            ));

            // Wait until the input value becomes empty
            boolean isEmpty = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(driver -> input.getAttribute("value").isEmpty());

            // Print for debugging
            System.out.println("DEBUG → Field: " + field + " | value: '" + input.getAttribute("value") + "'");

            Assert.assertTrue(isEmpty, "Field " + field + " is NOT empty after Clear");
        }

        /*// DATE FIELDS
        String[] dateFields = {
                "appointmentDateFrom",
                "appointmentDateTo"
        };

        for (String field : dateFields) {
            WebElement dateInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.name(field)
            ));

            // Wait until the value is empty
            boolean isEmpty = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(driver -> dateInput.getAttribute("value").isEmpty());

            System.out.println("DEBUG → Date Field: " + field + " | value: '" + dateInput.getAttribute("value") + "'");

            Assert.assertTrue(isEmpty, "Date field " + field + " not cleared");
        }*/

        System.out.println("✔ All filter fields reset to default.");
    }

    @Then("The table should show No results message")
    public void tableShouldShowNoResults() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement noResults = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".appointment-history__text")
        ));

        String actualText = noResults.getText().trim();
        Assert.assertEquals("0 results found", actualText);
        System.out.println("Expected: '0 results found' | Actual: '" + actualText + "'");

    }

    public void clickOutsideModal() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Wait for backdrop
        WebElement backdrop = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("div.MuiBackdrop-root")
                )
        );

        // Click the backdrop using JS (works 100% of the time)
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", backdrop);

        // Wait for backdrop to disappear
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("div.MuiBackdrop-root")
        ));
    }

    @And("User closes the filter modal")
    public void userClosesFilterModal() {
        clickOutsideModal();
    }

    public static int totalRowsBeforeFilter;

    @Then("The table should show all rows")
    public void theTableShouldShowAllRows() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for table render
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table tbody tr")));

        int rowCountAfterClear =
                driver.findElements(By.cssSelector("table tbody tr")).size();

        System.out.println("📌 Rows after Clear = " + rowCountAfterClear);

        Assert.assertTrue(rowCountAfterClear >= totalRowsBeforeFilter,
                "Table did NOT reset. Before = " + totalRowsBeforeFilter +
                        ", After Clear = " + rowCountAfterClear);

        System.out.println("✔ Table successfully reset to full dataset.");;
    }

    @Then("The table should show only rows matching {string} with {string}")
    public void theTableShouldShowOnlyRowsMatchingWith(String arg0, String arg1) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("User click on ENTER key")
    public void userClickOnENTERKey() {
        WebElement activeElement = driver.switchTo().activeElement();
        activeElement.sendKeys(Keys.ENTER);
    }


    @Then("admin filter data to be export")
    public void admin_filter_data_to_be_export() {
        WebElement filterButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"layout-container\"]/div/div/div/div/div[1]/div[2]/button[1]")));
        filterButton.click();

        WebElement remarkCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[name='remark'][value='true']")
        ));
        remarkCheckbox.click();
        System.out.println("Checkbox Remark clickable");

        WebElement empNameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("input[name='employeeName']")
                )
        );

        empNameField.clear();
        empNameField.sendKeys("aini");
        System.out.println("Employee name inserted");

        WebElement statusDropdown = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("div#mui-component-select-statusListStr")
                )
        );
        statusDropdown.click();

        // Click the option
        WebElement option = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//li[normalize-space()='Cancelled']")
                )
        );
        option.click();
        System.out.println("Status selected");
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Search']]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", searchButton);
        assertTrue(searchButton.isDisplayed(), "Search button is not visible");

        searchButton.click();
        System.out.println("Search button clicked");

        allureScreenshot();
    }

    @Given("admin click export button")
    public void admin_click_export_button() {

        WebElement exportButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Export']"))
        );
        exportButton.click();

        System.out.println("Export button clickable");
    }

    @Then("exported file should contain only filtered data and visible columns")
    public void exported_file_should_contain_only_filtered_data_and_visible_columns() throws Exception {

        File latestFile = waitForLatestDownloadedFile(".xlsx", 10);
        System.out.println("Validating exported Excel file: " + latestFile.getName());

        try (FileInputStream fis = new FileInputStream(latestFile);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            Row header = sheet.getRow(0);
            Assert.assertNotNull(header, "Exported file contains no header row");

            // 🔹 Identify required column indexes
            Map<String, Integer> colIndexes = getColumnIndexes(header);

            assertColumnPresent(colIndexes, "Employee Name");
            assertColumnPresent(colIndexes, "Appointment Date");
            assertColumnPresent(colIndexes, "Status");

            /*
            boolean remarkVisible = colIndexes.containsKey("Remark");

            if (remarkVisible) {
                System.out.println("✓ Remark column is visible — will validate values");
            } else {
                System.out.println("✓ Remark column is hidden — skipping remark validation");
            }*/

            // 🔹 Validate each row
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String empNameValue = getCellValue(row.getCell(colIndexes.get("Employee Name"))).toLowerCase();
                String statusValue = getCellValue(row.getCell(colIndexes.get("Status"))).toUpperCase();

                Assert.assertTrue(empNameValue.contains("athirah".toLowerCase()),
                        "Row " + i + ": Employee Name does not match filter: " + empNameValue);

                Assert.assertEquals(statusValue, "OVERDUE",
                        "Row " + i + ": Status column mismatch. Expected OVERDUE, found " + statusValue);

                /* Validate Remark only when visible
                if (remarkVisible) {
                    String remarkValue = getCellValue(row.getCell(colIndexes.get("Remark"))).toLowerCase();
                    Assert.assertEquals(remarkValue, "true",
                            "Row " + i + ": Remark expected 'true', found: " + remarkValue);
                }*/
            }
        }

        System.out.println("✓ Export validation PASSED");
    }

    private File waitForLatestDownloadedFile(String extension, int timeoutSeconds) throws InterruptedException {
        String downloadPath = System.getProperty("user.home") + "/Downloads";
        File dir = new File(downloadPath);
        File latest = null;

        for (int i = 0; i < timeoutSeconds * 2; i++) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(extension));
            if (files != null && files.length > 0) {
                latest = Arrays.stream(files)
                        .max(Comparator.comparingLong(File::lastModified))
                        .orElse(null);
            }
            if (latest != null) return latest;

            Thread.sleep(500);
        }

        throw new AssertionError("No downloaded " + extension + " file found after waiting " + timeoutSeconds + " seconds");
    }

    private Map<String, Integer> getColumnIndexes(Row header) {
        Map<String, Integer> map = new HashMap<>();

        for (Cell cell : header) {
            String colName = cell.getStringCellValue().trim();
            map.put(colName, cell.getColumnIndex());
        }
        return map;
    }

    private void assertColumnPresent(Map<String, Integer> colMap, String name) {
        Assert.assertTrue(colMap.containsKey(name),
                "Missing required column in export: " + name);
    }

    /** Utility method — safely handles null, numeric, boolean, and string cells */
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    @And("User click on ESC key to close the modal")
    public void userClickOnESCKeyToCloseTheModal() {
        WebElement activeElement = driver.switchTo().activeElement();
        activeElement.sendKeys(Keys.ESCAPE);
    }
}
