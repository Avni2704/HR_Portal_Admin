package steps;

import drivers.DriverInstance;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.testng.AssertJUnit.*;
import static qa.util.ExternalFunction.waitForLoaderToDisappear;

public class AdminDashboard extends DriverInstance {

    private WebDriverWait wait;
    private String expectedStatus;

    //TC-A016
    @Given("admin on the login page")
    public void admin_on_the_login_page() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\naqiy\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
            driver = new ChromeDriver(options);
        }
        driver.manage().window().maximize();
        driver.get("https://hrms.admin.uat.directintegrate.com/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Given("the email {string}")
    public void the_email(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(":r0:"))).sendKeys(email);
    }

    @Given("the password {string}")
    public void the_password(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(":r1:"))).sendKeys(password);
    }

    @When("admin clicked login")
    public void admin_clicked_login() {
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Login']]")));
        loginButton.click();
    }

    @When("admin select company modal")
    public void admin_see_select_company_modal() throws InterruptedException {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement selectCompany = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[2]/div[3]")));

        assertTrue("Select company modal not visible.", selectCompany.isDisplayed());

        WebElement dropdownCompany = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"mui-component-select-company\"]")));
        dropdownCompany.click();

        WebElement company = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='Mango Parfait']")));
        company.click();
        Thread.sleep(1000);

        WebElement confirm = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//p[text()='Confirm']]")));
        confirm.click();

        System.out.println("Select company loaded successfully.");
    }

    @Given("admin see the dashboard")
    public void admin_see_the_dashboard() throws InterruptedException {

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement dashboardElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"layout-container\"]/header/div/h1")

        ));

        assertTrue("Admin dashboard element not visible.", dashboardElement.isDisplayed());
        System.out.println("Admin dashboard loaded successfully.");
        waitForLoaderToDisappear(driver);
        Thread.sleep(1000);
        allureScreenshot();
    }

    //TC-A017
    @Given("admin see username and greeting")
    public void admin_see_username_and_greeting() {

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement greetingElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"layout-container\"]/div/div/div/h1")
        ));

        assertTrue("Greeting element not visible.", greetingElement.isDisplayed());

        String displayedGreeting = greetingElement.getText().trim();
        System.out.println("DEBUG: Displayed greeting = [" + displayedGreeting + "]");

        LocalTime now = LocalTime.now();
        String expectedGreeting;

        if (now.isBefore(LocalTime.NOON)) { // before 12:00
            expectedGreeting = "Good Morning";
        } else if (now.isBefore(LocalTime.of(18, 0))) { // before 18:00
            expectedGreeting = "Good Afternoon";
        } else { // before midnight
            expectedGreeting = "Good Evening";
        }

        assertTrue(
                "Greeting does not match! Expected greeting to contain: " + expectedGreeting +
                        " but was: " + displayedGreeting,
                displayedGreeting.contains(expectedGreeting)
        );

        System.out.println("Greeting is correct: " + displayedGreeting);
        allureScreenshot();
    }

    //TC-A018
    @Given("admin see the current date")
    public void admin_see_the_current_date() {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement dateElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"layout-container\"]/div/div/div/p")
        ));

        assertTrue("Date element not visible.", dateElement.isDisplayed());

        String displayedDate = dateElement.getText().trim();
        System.out.println("DEBUG: Dashboard displayed date = [" + displayedDate + "]");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.ENGLISH);
        String todayDate = LocalDate.now().format(formatter);

        assertEquals("Displayed date does not match today's date!", todayDate, displayedDate);

        System.out.println("Employee dashboard shows correct current date: " + displayedDate);
        allureScreenshot();
    }

    //TC-A019
    @Given("admin see the announcements section")
    public void admin_see_the_announcements_section() {

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement announcementElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[1]")
        ));
        assertTrue("Announcements not visible.", announcementElement.isDisplayed());

        List<WebElement> noAnnouncements = driver.findElements(
                By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[1]/ul/div/div")
        );

        if (!noAnnouncements.isEmpty()) {
            System.out.println("No announcements available on the dashboard.");
        } else {
            List<WebElement> announcements = driver.findElements(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[1]/ul/li")
            );

            if (announcements.isEmpty()) {
                System.out.println("Neither announcements nor 'no announcements' message found.");
            } else {
                WebElement firstAnnouncementTitle = announcements.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[1]/ul/li/div[1]/p[1]"));
                WebElement firstAnnouncementContent = announcements.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[1]/ul/li/div[1]/p[2]"));

                assertTrue("Admin announcement element not visible.", firstAnnouncementTitle.isDisplayed());
                assertTrue("Admin announcement content element not visible.", firstAnnouncementContent.isDisplayed());

                System.out.println("Announcements section is visible with content.");
            }
        }

        allureScreenshot();
    }

    //TC-A020
    @Given("admin see the holiday section")
    public void admin_see_the_holiday_section() {

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement holidayElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]")
        ));
        assertTrue("Admin holiday element not visible.", holidayElement.isDisplayed());

        List<WebElement> noHoliday = driver.findElements(
                By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]/ul/div")
        );

        if (!noHoliday.isEmpty()) {
            System.out.println("No holiday available on the dashboard.");
        } else {
            List<WebElement> holiday = driver.findElements(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]/ul/li")
            );

            if (holiday.isEmpty()) {
                System.out.println("Neither holiday nor 'no holidays' message found.");
            } else {
                WebElement firstHolidayTitle = holiday.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]/ul/li/div/p[1]"));
                WebElement firstHolidayState = holiday.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]/ul/li/div/p[2]"));
                WebElement firstHolidayDate = holiday.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]/ul/li/p"));

                assertTrue("Admin holiday title not visible.", firstHolidayTitle.isDisplayed());
                assertTrue("Admin holiday state not visible.", firstHolidayState.isDisplayed());
                assertTrue("Admin holiday date not visible.", firstHolidayDate.isDisplayed());

                System.out.println("Holiday section is visible with content.");
            }
        }
        allureScreenshot();
    }

    //TC-A021
    @Given("admin click logout")
    public void admin_click_logout() {
        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"side-nav\"]/div/nav/div/div")));
        logoutButton.click();
        allureScreenshot();
    }

    //TC-A022
    @Given("admin click leave management")
    public void admin_click_leave_management() {

        WebElement leaveManagementButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"side-nav\"]/div/nav/ul/li[4]/div/div")));
        leaveManagementButton.click();

    }

    @Then("admin see leave calender page")
    public void admin_see_leave_calender_page() throws InterruptedException {

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement leavePage = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"layout-container\"]/div/div/div/div/div/div")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", leavePage);
        assertTrue("Leave management page not visible.", leavePage.isDisplayed());
        Thread.sleep(2000);
        allureScreenshot();
    }

    //TC-A023
    @Given("admin click allowance management")
    public void admin_click_allowance_management() {

        WebElement allowanceManagementButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"side-nav\"]/div/nav/ul/li[5]/div/div")));
        allowanceManagementButton.click();

    }

    @Then("admin see allowance directory page")
    public void admin_see_allowance_directory_page() throws InterruptedException {

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement allowancePage = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"layout-container\"]/header/div/h1")));

        assertTrue("Allowance management page not visible.", allowancePage.isDisplayed());
        Thread.sleep(1000);
        allureScreenshot();
    }

    //TC-A024
    @Given("admin click appointment management")
    public void admin_click_appointment_management() {

        WebElement appointmentManagementButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"side-nav\"]/div/nav/ul/li[6]/div/div")));
        appointmentManagementButton.click();

    }

    @Then("admin see appointment list page")
    public void admin_see_appointment_list_page() throws InterruptedException {

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement appointmentPage = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"layout-container\"]/header/div/h1")));

        assertTrue("Appointment management page not visible.", appointmentPage.isDisplayed());
        Thread.sleep(1000);
        allureScreenshot();
    }

    //TC-A025
    @Given("admin click dashboard")
    public void admin_click_dashboard() {

        WebElement appointmentManagementButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"side-nav\"]/div/nav/ul/li[1]/div/div")));
        appointmentManagementButton.click();
    }

    //TC-A026
    @Given("admin click profile icon")
    public void admin_click_profile_icon() {
        WebElement profileIconButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"layout-container\"]/header/div/div/div[2]")));
        profileIconButton.click();
    }


    @Given("admin see dropdown and change password")
    public void admin_see_dropdown_and_change_password() {

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement dropdown = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[2]/div[3]/ul")
        ));
        assertTrue("Admin dropdown not visible.", dropdown.isDisplayed());
        System.out.println("Admin dropdown loaded successfully.");

        WebElement changePassword = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[normalize-space()='Change Password']")
        ));
        assertTrue("Admin change password not visible.", changePassword.isDisplayed());
        allureScreenshot();
    }

    //TC-A027
    @Given("admin see celebration corner")
    public void admin_see_celebration_corner() throws InterruptedException {

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement celebrationElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]")
        ));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", celebrationElement);
        assertTrue("Admin Celebration Corner element not visible.", celebrationElement.isDisplayed());

        List<WebElement> noCelebration = driver.findElements(
                By.xpath("//p[@class='empty-data__card-description' and text()='No celebrations this month']")
        );

        if (!noCelebration.isEmpty()) {
            System.out.println("No Celebration available on the dashboard.");
        } else {
            List<WebElement> celebration = driver.findElements(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]/ul/li")
            );

            if (celebration.isEmpty()) {
                System.out.println("Neither celebration nor 'no celebration' message found.");
            } else {
                WebElement celebrationProfile = celebration.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]/ul/li/div"));
                WebElement celebrationName = celebration.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]/ul/li/p[1]"));
                WebElement celebrationDate = celebration.get(0).findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]/ul/li/p[2]"));

                assertTrue("Admin celebration profile not visible.", celebrationProfile.isDisplayed());
                assertTrue("Admin celebration name not visible.", celebrationName.isDisplayed());
                assertTrue("Admin celebration date not visible.", celebrationDate.isDisplayed());

                System.out.println("Celebration section is visible with content.");
            }
        }
        Thread.sleep(1000);
        allureScreenshot();
    }

    //TC-A028
    @Given("admin click bell icon")
    public void admin_click_bell_icon() throws InterruptedException {
        WebElement bellIconButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("img[alt='notifications']")));
        bellIconButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Thread.sleep(1000);
        System.out.println("Notification icon is visible on the dashboard.");
        allureScreenshot();
    }

    @Then("admin view notifications")
    public void admin_view_notifications() {
        //Test
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        WebElement notifModal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.notification-modal")));
        assertTrue("Notification modal not visible!", notifModal.isDisplayed());

        WebElement headerTitle = notifModal.findElement(By.cssSelector(".notification-modal__header--row--title"));
        assertEquals("Notifications", headerTitle.getText().trim());

        System.out.println("Notification list is visible in the modal");
        allureScreenshot();
    }

    //TC-A029
    @Given("admin click company icon")
    public void admin_click_company_icon() throws InterruptedException {
        WebElement companyIconButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"side-nav\"]/div/div/div")));
        companyIconButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Thread.sleep(1000);
        allureScreenshot();
    }

    //TC-A030
    @Given("admin see holidays displayed in ascending order by date")
    public void admin_see_holidays_displayed_in_ascending_order_by_date() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]"))
        );

        List<WebElement> rows = card.findElements(By.xpath(".//ul/li"));

        if (rows.isEmpty()) {
            List<WebElement> noHolidayMsg = card.findElements(By.xpath(".//p[@class='empty-data__card-description' and text()='No holiday this month']"));
            assertFalse("No holidays found, but also no 'No holiday this month' message displayed.", noHolidayMsg.isEmpty());
            System.out.println("No holidays this month (message displayed).");
            allureScreenshot();
            return;
        }

        List<Integer> actualDates = new ArrayList<>();
        for (WebElement row : rows) {
            WebElement rightCell = row.findElement(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]/ul/li/p")); // relative XPath inside row
            String dateText = rightCell.getText().trim();

            String digits = dateText.replaceAll("\\D", "");
            assertFalse("No day number found in row text: '" + dateText + "'", digits.isEmpty());

            actualDates.add(Integer.parseInt(digits));
        }

        List<Integer> sortedDates = new ArrayList<>(actualDates);
        Collections.sort(sortedDates);

        assertEquals("Holidays are not sorted in ascending order by date!", sortedDates, actualDates);

        System.out.println("Holidays are correctly sorted by date in ascending order: " + actualDates);
        allureScreenshot();
    }

    //TC-A031
    @Given("dashboard responsive and elements realign")
    public void dashboard_responsive_and_elements_realign() throws InterruptedException {
        int[][] viewports = {
                {1920, 1080},
                {1500, 817},
                {850, 817},
                {600, 500}
        };

        for (int[] size : viewports) {
            int width = size[0];
            int height = size[1];
            driver.manage().window().setSize(new Dimension(width, height));
            ((JavascriptExecutor) driver).executeScript("window.dispatchEvent(new Event('resize'));");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            Thread.sleep(1000);

            //check Announcement is visible
            WebElement announcement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[1]")));
            assertTrue("Announcement is not visible at " + width + "x" + height, announcement.isDisplayed());

            //check Holidays card is visible
            WebElement holidays = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[2]")));
            assertTrue("Holiday card is not visible at " + width + "x" + height, holidays.isDisplayed());

            //check Celebration Corner card is visible
            WebElement celebrationCorner = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[1]")));
            assertTrue("Celebration Corner card is not visible at " + width + "x" + height, celebrationCorner.isDisplayed());

            //check Upcoming Leave card is visible
            WebElement employeeOnLeave = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[2]")));
            assertTrue("Employee on Leave card is not visible at " + width + "x" + height, employeeOnLeave.isDisplayed());

            //check Leave Balance card is visible
            WebElement totalEmployee = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[3]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", totalEmployee);
            assertTrue("Leave Balance card is not visible at " + width + "x" + height, totalEmployee.isDisplayed());

            System.out.println("Dashboard responsive at " + width + "x" + height);
            allureScreenshot();
        }
    }

    //TC-A032

    //TC-A033
    @Given("admin click change password dropdown")
    public void admin_click_change_password_dropdown() throws InterruptedException {
        WebElement changePasswordButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[2]/div[3]/ul/li")));
        changePasswordButton.click();

        WebElement changePasswordTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[2]/div[3]")));
        assertTrue("Admin change password tab is not visible at ", changePasswordTab.isDisplayed());
        Thread.sleep(1000);
        allureScreenshot();
    }

    //TC-A034
    @Given("admin click create announcement")
    public void admin_click_create_announcement() throws InterruptedException {
        WebElement createAnnouncementButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Create Announcement']]")));
        createAnnouncementButton.click();

        WebElement createAnnouncementTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[2]/div[3]")));
        assertTrue("Admin create announcement modal is not visible at ", createAnnouncementTab.isDisplayed());
        Thread.sleep(1000);
        System.out.println("Admin create announcement modal is visible.");
        allureScreenshot();
    }

    //TC-A035
    @Then("admin confirm remaining announcement")
    public void admin_confirm_remaining_announcement() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Posted tab
        WebElement postedAnnouncementButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[1]/div[2]/ul/li[1]"))
        );
        postedAnnouncementButton.click();

        List<WebElement> posted = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//ul[contains(@class,'announcements--scrollable')]/li[contains(@class,'announcements__item')]")
                )
        );
        int postedCount = posted.size();
        System.out.println("Posted announcements: " + postedCount);

        // Upcoming tab
        WebElement upcomingAnnouncementButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[1]/div[2]/ul/li[2]"))
        );
        upcomingAnnouncementButton.click();

        List<WebElement> upcoming = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//ul[contains(@class,'announcements--scrollable')]/li[contains(@class,'announcements__item')]")
                )
        );
        int upcomingCount = upcoming.size();
        System.out.println("Upcoming announcements: " + upcomingCount);

        // Total
        int total = postedCount + upcomingCount;
        System.out.println("Total created announcements: " + total);

        WebElement remainingLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[1]/div[1]/div[2]/p/span"))
        );
        String labelText = remainingLabel.getText().trim();
        int remaining = Integer.parseInt(labelText.replaceAll("\\D", ""));

        int expectedRemaining = 15 - total;
        assertEquals("Remaining announcement count mismatch!", expectedRemaining, remaining);

        System.out.println("Remaining announcements correct. Posted: " + posted.size() + ", Upcoming: " + upcoming.size() +
                ", Remaining: " + remaining);
        allureScreenshot();
    }

    //TC-A036
    @Given("admin switch posted and upcoming announcement")
    public void admin_switch_posted_and_upcoming_announcement() throws InterruptedException {

        WebElement postedAnnouncementButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[1]/div[2]/ul/li[1]"))
        );
        postedAnnouncementButton.click();

        WebElement upcomingAnnouncementButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[1]/div[1]/div[2]/ul/li[2]")));
        upcomingAnnouncementButton.click();
        Thread.sleep(1000);
        allureScreenshot();

        postedAnnouncementButton.click();

        System.out.println("Admin able to change posted and upcoming announcement");
    }

    //TC-A037
    @Then("admin view active announcement")
    public void admin_view_active_announcement() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> announcements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//ul[contains(@class,'announcements--scrollable')]/li[contains(@class,'announcements__item')]")
        ));

        boolean foundActive = false;

        for (WebElement announcement : announcements) {
            WebElement statusElement = announcement.findElement(By.xpath(".//p[@class='status__text']"));
            String actualStatus = statusElement.getText().trim();

            if (actualStatus.equalsIgnoreCase("Active")) {
                foundActive = true;
                break;
            }
        }

        assertTrue("No Active announcement found in Posted tab!", foundActive);
    }

    @Then("admin view active announcement with green tag")
    public void admin_view_active_announcement_with_green_tag() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Locate all status elements
        List<WebElement> statusElements = wait.until(ExpectedConditions
                .visibilityOfAllElementsLocatedBy(By.cssSelector("div.announcements__status p.status__text"))
        );

        List<WebElement> activeStatuses = new ArrayList<>();

        for (WebElement statusElement : statusElements) {
            String statusText = statusElement.getText().trim();

            if (statusText.equalsIgnoreCase("Active")) {
                activeStatuses.add(statusElement);

                String bgColor = statusElement.findElement(By.xpath("..")).getCssValue("background-color");

                String fontColor = statusElement.getCssValue("color");

                String expectedBgColor = "rgba(0, 207, 156, 0.1)";
                String expectedFontColor = "rgba(0, 207, 156, 1)";

                assertEquals("Background color mismatch for Active status", expectedBgColor, bgColor);
                assertEquals("Font color mismatch for Active status", expectedFontColor, fontColor);
            }
        }

        System.out.println("Total Active Announcements: " + activeStatuses.size());

        assertTrue("No Active announcements found", activeStatuses.size() > 0);

        allureScreenshot();
    }

    @Then("admin create announcement {string} start {string} end {string} description {string}")
    public void admin_create_announcement(String expectedTitle, String pickStartDate, String pickEndDate, String description) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement createAnnouncementButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Create Announcement']]")));
        createAnnouncementButton.click();

        WebElement createAnnouncementTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[2]/div[3]")));
        assertTrue("Admin create announcement modal is not visible", createAnnouncementTab.isDisplayed());

        WebElement titleField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Title']")));
        titleField.clear();
        titleField.sendKeys(expectedTitle);

        //Pick date
        WebElement startCalendar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[2]/div[3]/div/form/div[2]/div/div")));
        startCalendar.click();

        WebElement startDate = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[3]/div[3]/div[1]/div/ul/li["+pickStartDate+"]")));
        startDate.click();

        WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Ok']]")));
        okButton.click();
        System.out.println("start date selected");

        WebElement endCalendar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[2]/div[3]/div/form/div[3]/div/div")));
        endCalendar.click();

        /*WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@class='header__next']")));
        nextButton.click();*/

        WebElement endDate = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[3]/div[3]/div[1]/div/ul/li["+pickEndDate+"]")));
        endDate.click();

        WebElement okButton2 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Ok']]")));
        okButton2.click();
        System.out.println("end date selected");


        // Fill Announcement Description
        WebElement descField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@placeholder='Description']")));
        descField.clear();
        descField.sendKeys(description);
        allureScreenshot();

        // Save button
        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Create']]")));
        saveBtn.click();
        System.out.println("new announcement created");

    }

    @Then("admin see the new announcement {string}")
    public void admin_see_the_new_announcement(String expectedTitle) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        WebElement newAnnouncement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//ul[contains(@class,'announcements--scrollable')]//li[contains(@class,'announcements__item')]//div[contains(@class,'announcements__content')]//p[contains(@class,'announcements__title') and text()='" + expectedTitle + "']"))
        );


        assertTrue("New announcement with title '" + expectedTitle + "' is not visible",
                newAnnouncement.isDisplayed());

        System.out.println("admin see the new created announcement");
    }

    @Then("admin see new announcement status {string} must be {string}")
    public void admin_see_new_announcement_status(String expectedTitle, String expectedStatus) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement announcementItem = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//ul[contains(@class,'announcements--scrollable')]//li[contains(@class,'announcements__item')]"
                        + "[.//p[contains(@class,'announcements__title') and text()='" + expectedTitle + "']]")
        ));

        WebElement statusTag = announcementItem.findElement(By.xpath(".//p[contains(@class,'status__text')]"));

        assertEquals("Status for announcement '" + expectedTitle + "' does not match",
                expectedStatus, statusTag.getText().trim());

        System.out.println("Status for announcement '" + expectedTitle + "': " + statusTag.getText().trim());
        allureScreenshot();
    }

    //TC-A040
    @Then("admin view expired announcement with red tag")
    public void admin_view_expired_announcement_with_red_tag() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Locate all status elements
        List<WebElement> statusElements = wait.until(ExpectedConditions
                .visibilityOfAllElementsLocatedBy(By.cssSelector("div.announcements__status p.status__text"))
        );

        List<WebElement> activeStatuses = new ArrayList<>();

        for (WebElement statusElement : statusElements) {
            String statusText = statusElement.getText().trim();

            if (statusText.equalsIgnoreCase("Expired")) {
                activeStatuses.add(statusElement);

                String bgColor = statusElement.findElement(By.xpath("..")).getCssValue("background-color");

                String fontColor = statusElement.getCssValue("color");

                String expectedBgColor = "rgba(246, 68, 68, 0.1)";
                String expectedFontColor = "rgba(246, 68, 68, 1)";

                assertEquals("Background color mismatch for Expired status", expectedBgColor, bgColor);
                assertEquals("Font color mismatch for Expired status", expectedFontColor, fontColor);
            }
        }

        System.out.println("Total Expired Announcements: " + activeStatuses.size());

        assertTrue("No Expired announcements found", activeStatuses.size() > 0);

        allureScreenshot();
    }

    @Given("admin click pin icon and identify color change")
    public void admin_click_pin_icon_and_identify_color_change() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement pinButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@class,'announcements__button--pin')]")
        ));
        assertTrue("Pin button is not visible",pinButton.isDisplayed());
        pinButton.click();

        WebElement pinnedButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@class,'announcements__button--pinned')]")
        ));
        assertTrue("Pinned button is not visible",pinnedButton.isDisplayed());
        System.out.println("admin can click pin button");
        allureScreenshot();
    }

    @Given("admin see pin announcement banner")
    public void admin_see_pin_announcement_banner() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement banner = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='app-alert']/div")
        ));
        assertTrue("Banner is not visible",banner.isDisplayed());
        System.out.println("admin see pin announcement banner");
        allureScreenshot();
    }

    @Given("admin click edit icon")
    public void admin_click_edit_icon(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement moreButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@class,'announcements__button--more')]")
        ));
        assertTrue("More button is not visible",moreButton.isDisplayed());
        moreButton.click();

        WebElement editButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[img[@alt='edit'] and contains(normalize-space(.),'Edit')]")
        ));
        assertTrue("Edit button is not visible",editButton.isDisplayed());
        editButton.click();
    }

    @Then("admin see create announcement tab")
    public void admin_see_create_announcement_tab() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement createAnnouncementModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'create-announcement-modal__main')]")
        ));
        assertTrue("Create announcement modal is not visible",createAnnouncementModal.isDisplayed());
        System.out.println("admin can see create announcement modal");
        Thread.sleep(1000);
        allureScreenshot();
    }

    @Then("admin see employee on leave with details")
    public void admin_see_employee_on_leave_with_details() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement employeeOnLeave = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"layout-container\"]/div/div/div/div[2]/div[2]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", employeeOnLeave);
        assertTrue("Employee on leave with details is not visible",employeeOnLeave.isDisplayed());

        WebElement employeeName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='leaves__name']")
        ));
        assertTrue("Employee name is not visible",employeeName.isDisplayed());

        WebElement leaveType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='leaves__balance']")
        ));
        assertTrue("Employee leave type is not visible",leaveType.isDisplayed());

        System.out.println("admin can see employee on leave with details");
        Thread.sleep(1000);
        allureScreenshot();
    }

    @Given("admin click view all")
    public void admin_click_view_all(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement viewAll = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='dashboard__label' and text()='View All']")
        ));
        assertTrue("View all button is not visible",viewAll.isDisplayed());
        viewAll.click();
    }

    //TC-A047
    @Given("admin click user management")
    public void admin_click_user_management() {

        WebElement userManagementButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"side-nav\"]/div/nav/ul/li[2]/div")));
        userManagementButton.click();

    }

    @Then("admin see list employee page")
    public void admin_see_list_employee_page() throws InterruptedException {

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement userPage = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"layout-container\"]/div")));

        assertTrue("User management page not visible.", userPage.isDisplayed());
        Thread.sleep(1000);
        allureScreenshot();
    }

    //TC-A048
    @Given("admin click company benefits")
    public void admin_click_company_benefits() {

        WebElement userManagementButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"side-nav\"]/div/nav/ul/li[3]/div")));
        userManagementButton.click();

    }

    @Then("admin see job position list page")
    public void admin_see_job_position_list_page() throws InterruptedException {

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement userPage = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"layout-container\"]/div")));

        assertTrue("Company Benefits page not visible.", userPage.isDisplayed());
        Thread.sleep(1000);
        allureScreenshot();
    }

    @Given("admin click delete icon")
    public void admin_click_delete_icon(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement moreButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@class,'announcements__button--more')]")
        ));
        assertTrue("More button is not visible",moreButton.isDisplayed());
        moreButton.click();

        WebElement deleteButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[contains(@class,'app-table-menu__remove')]//img[@alt='delete']")
        ));
        assertTrue("Delete button is not visible",deleteButton.isDisplayed());
        deleteButton.click();
    }

    @Given("admin see delete announcement banner")
    public void admin_see_delete_announcement_banner() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement banner = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='alert']//p[@class='alert__text' and text()='Announcement has been deleted successfully']")
        ));
        assertTrue("Banner is not visible",banner.isDisplayed());
        System.out.println("admin see delete announcement banner");
        allureScreenshot();
    }

    @Given("admin see remaining")
    public void admin_see_remaining() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement remainingElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[@class='dashboard__remaining']/span")
        ));

        String remainingValue = remainingElement.getText().trim();
        System.out.println("Admin sees remaining announcement: " + remainingValue);

        assertEquals("Remaining announcements should be 0", "0", remainingValue);

        allureScreenshot();
    }

    @Given("admin see disabled button")
    public void admin_see_disabled_button() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        System.out.println("Admin sees Create Announcement button is disabled");

        WebElement createBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button.app-button[disabled]")
        ));

        assertFalse("Create Announcement button should be disabled", createBtn.isEnabled());

        allureScreenshot();
    }

    @Given("admin see validation error")
    public void admin_see_validation_error() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement startDateError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(@class,'MuiFormHelperText-root') and text()='Start Date must be before end date']")
        ));
        assertTrue("Start Date Error is not visible",startDateError.isDisplayed());

        WebElement endDateError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(@class,'MuiFormHelperText-root') and text()='End Date must be after start date']")
        ));
        assertTrue("End Date Error is not visible",endDateError.isDisplayed());

        System.out.println("admin see validation error");
        allureScreenshot();
    }

    @Given("admin insert title {string}")
    public void admin_insert_title(String expectedTitle) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement titleField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Title']")));
        titleField.clear();
        titleField.sendKeys(expectedTitle);
    }

    @Given("admin insert start date {string}")
    public void admin_insert_start_date(String pickStartDate) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //Pick start date
        WebElement startCalendar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[2]/div[3]/div/form/div[2]/div/div")));
        startCalendar.click();

        WebElement startDate = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(@class,'dates__item') and not(contains(@class,'previous')) and not(contains(@class,'next'))]//div[@class='dates__day' and text()='" + pickStartDate + "']")));
        startDate.click();

        WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Ok']]")));
        okButton.click();
        System.out.println("start date selected");
    }

    @Given("admin insert end date {string}")
    public void admin_insert_end_date(String pickEndDate) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //Pick end date
        WebElement endCalendar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[2]/div[3]/div/form/div[3]/div/div")));
        endCalendar.click();

        WebElement endDate = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(@class,'dates__item') and not(contains(@class,'previous')) and not(contains(@class,'next'))]//div[@class='dates__day' and text()='" + pickEndDate + "']")));
        endDate.click();

        WebElement okButton2 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Ok']]")));
        okButton2.click();
        System.out.println("end date selected");
    }

    @Given("admin insert description {string}")
    public void admin_insert_description(String description) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Fill Announcement Description
        WebElement descField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@placeholder='Description']")));
        descField.clear();
        descField.sendKeys(description);
        System.out.println("Description inserted");
        allureScreenshot();

    }

    @Given("admin upload attachment {string}")
    public void admin_upload_attachment(String attachment) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@class='create-announcement-modal__input']//input[@type='file']")
        ));

        fileInput.sendKeys(attachment);

        WebElement uploadButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button.create-announcement-modal__upload-button")
        ));
        String uploadedFileName = uploadButton.getText();
        System.out.println("Uploaded file: " + uploadedFileName);

        allureScreenshot();
    }

    @Given("admin click create button")
    public void admin_click_create_button() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Save button
        WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Create']]")));
        createButton.click();
        System.out.println("new announcement created");
    }

    @Given("admin see announcement banner")
    public void admin_see_announcement_banner() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement banner = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='alert']//p[@class='alert__text' and text()='Announcement has been created successfully']")
        ));
        assertTrue("Banner is not visible",banner.isDisplayed());
        System.out.println("admin see green announcement banner");
        allureScreenshot();
    }

    @Given("admin click cancel button")
    public void admin_click_cancel_button() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Save button
        WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@class='app-button app-button--outline']//p[text()='Cancel']")));
        cancelButton.click();
        System.out.println("new announcement cancelled");
    }

    @Given("admin verify data inserted not saved {string}")
    public void admin_verify_data_inserted_not_saved(String expectedTitle) {
        List<WebElement> announcementData = driver.findElements(By.xpath("//input[@placeholder='"+expectedTitle+"']"));
        assertTrue("Cancelled announcement should not appear in the list", announcementData.isEmpty());

        System.out.println("new announcement cancelled");
    }

    @Given("admin see validation message")
    public void admin_see_validation_message() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement startDateError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(@class,'MuiFormHelperText-root') and text()='This field is required.']")
        ));
        assertTrue("Validation Error Message is not visible",startDateError.isDisplayed());

        System.out.println("admin see validation error message");
        allureScreenshot();
    }

    @Then("verify description max length")
    public void verify_description_max_length() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement descField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//textarea[@name='description' or @placeholder='Description']")));

        String actualValue = descField.getAttribute("value").trim();
        int actualLength = actualValue.length();

        System.out.println("Actual description length: " + actualLength);
        assertTrue("Description field allows more than 255 characters", actualLength <= 255);

        allureScreenshot();
    }

    @Given("admin see file validation")
    public void admin_see_file_validation() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement startDateError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='alert']/p[@class='alert__text' and text()='Invalid file type.']")
        ));
        assertTrue("File Error is not visible",startDateError.isDisplayed());


        System.out.println("admin see validation error");
        allureScreenshot();
    }

    @Given("admin see large file validation")
    public void admin_see_large_file_validation() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement largeFileError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='create-announcement-modal__error' and text()='File Size is too large (maximum 5MB)']")
        ));
        assertTrue("File validation not visible",largeFileError.isDisplayed());

        System.out.println("admin see validation error");
        allureScreenshot();
    }

    @Given("admin see updated announcement banner")
    public void admin_see_updated_announcement_banner() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement banner = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='alert']/p[@class='alert__text' and text()='Announcement has been updated successfully']")
        ));
        assertTrue("Banner is not visible",banner.isDisplayed());
        System.out.println("admin see green updated announcement banner");
        allureScreenshot();
    }


    private String storedTitle;
    private String storedDescription;

    @Given("admin see announcement details")
    public void admin_see_announcement_details() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("p.announcements__title")));
        WebElement descElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("p.announcements__description")));

        storedTitle = titleElement.getText().trim();
        storedDescription = descElement.getText().trim();

        System.out.println("Captured Announcement Details:");
        System.out.println("Title: " + storedTitle);
        System.out.println("Description: " + storedDescription);
        allureScreenshot();
    }

    @Then("admin verify announcement pre-filled")
    public void admin_verify_announcement_pre_filled() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement titleField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[name='title'][placeholder='Title']")
        ));
        WebElement descField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("textarea[name='description'][placeholder='Description']")
        ));

        String actualTitle = titleField.getAttribute("value").trim();
        String actualDescription = descField.getAttribute("value").trim();

        System.out.println("Expected Title: " + storedTitle);
        System.out.println("Actual Title: " + actualTitle);
        System.out.println("Expected Description: " + storedDescription);
        System.out.println("Actual Description: " + actualDescription);

        assertEquals("Title not prefilled correctly", storedTitle, actualTitle);
        assertEquals("Description not prefilled correctly", storedDescription, actualDescription);

        allureScreenshot();
    }

    //TC-A081
    @When("page minimise")
    public void page_minimise() {
        driver.manage().window().setSize(new Dimension(800, 900));
        allureScreenshot();
    }

    @Given("admin click admin list")
    public void admin_click_admin_list() {

        WebElement adminListButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"side-nav\"]/div/nav/ul/li[2]/div[3]")));
        adminListButton.click();

        System.out.println("Admin List button clickable");
        allureScreenshot();
    }

}