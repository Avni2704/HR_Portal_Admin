package steps;

import drivers.DriverInstance;

import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class AdminLoginSteps extends DriverInstance {

    private WebDriverWait wait;

    //TC001
    @Given("user is on the login page")
    public void user_is_on_the_login_page() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            System.setProperty("webdriver.chrome.driver", "D:\\Selenium\\chromedriver-win64\\chromedriver.exe");
            driver = new ChromeDriver(options);
        }
        driver.manage().window().maximize();
        driver.get("https://hrms.admin.uat.directintegrate.com/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Given("the email is {string}")
    public void the_email_is(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(":r0:"))).sendKeys(email);
    }

    @Given("the password is {string}")
    public void the_password_is(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(":r1:"))).sendKeys(password);
    }

    @When("user click login")
    public void user_click_login() {
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Login']]")));
        loginButton.click();
    }


    @Then("user can see the dashboard")
    public void user_can_see_the_dashboard() {

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));

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

        assertTrue("Admin dashboard element not visible.", dashboardElement.isDisplayed());

        System.out.println("Admin dashboard loaded successfully.");

        allureScreenshot();
    }

    //TC002
    @Given("user check Remember Me")
    public void user_check_remember_me() {
        WebElement rememberMeButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"root\"]/div[2]/div/main/div[2]/form/div[3]/button")));
        rememberMeButton.click();

        // Optional: Verify it’s checked
        // wait.until(ExpectedConditions.attributeContains(rememberMeButton, "class", "app-checkbox--active"));
    }

    /*@And("user select company")
    public void user_select_company() {
        WebElement selectCompany = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div[role='button']")));
        selectCompany.click();

        WebElement companyName = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='Your Company Name']")));
        companyName.click();

        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[p[text()='Confirm']]")));
        confirmButton.click();

        wait.until(ExpectedConditions.urlContains("dashboard"));
        allureScreenshot();
    }*/

    //TC003
    @When("user click Enter")
    public void userClickEnter() {
        WebElement passwordField = driver.findElement(By.id(":r1:"));
        passwordField.sendKeys("Tester@123");
        passwordField.sendKeys(Keys.ENTER);
    }

    //TC004
    @When("user click the eye icon")
    public void userClickTheEyeIcon() throws InterruptedException {
        WebElement toggleButton = driver.findElement(By.className("app-icon")); // adjust selector
        toggleButton.click();

        Thread.sleep(5000);
    }

    @Then("user can view the password")
    public void userCanViewThePassword() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[1]/div[2]/div/main/div[2]/form/div[2]/div/div/div/input")
        ));

        String type = passwordField.getAttribute("type");
        assertEquals(type, "text"); // expected, actual

        allureScreenshot();
    }

    //TC005
    @When("user click on Forgot Password button")
    public void userClickOnForgotPasswordButton() throws InterruptedException {
        WebElement forgotPassword = driver.findElement(By.className("main__forgot-password"));
        forgotPassword.click();

        Thread.sleep(1000);
    }

    @Then("user is redirected to password recovery page")
    public void userIsRedirectedToPasswordRecoveryPage() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("forgot-password"));

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("forgot-password"));

        allureScreenshot();
    }

    //TC006
    @Then("user open new browser")
    public void user_open_new_browser() {
        driver.get("https://hrms.admin.uat.directintegrate.com/");
        driver.manage().window().maximize();
    }

    @And("user can see existing dashboard")
    public void userCanSeeExistingDashboard() {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement dashboardElement = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[1]/div[2]/main/div[2]/header/div/h1")
        ));

        assertTrue("Admin dashboard element not visible.", dashboardElement.isDisplayed());

        System.out.println("Admin dashboard loaded successfully.");

        allureScreenshot();
    }


    //TC007
    @When("minimise the page size")
    public void minimiseThePageSize() {
        driver.manage().window().setSize(new Dimension(300, 500));
        allureScreenshot();
    }

    @Then("contents are visible")
    public void contentsAreVisible() {
        // Check if the login page is still visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement loginPage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div/div[2]/div/main/div[2]/form/h1")
        ));
        assertEquals(loginPage.getText(), "Welcome Back!");
        //driver.quit();
        allureScreenshot();
    }

    //TC008
    @Then("the placeholders for email and password should be {string} and {string}")
    public void the_placeholders_for_email_and_password_should_be_and(String expectedPlaceholderEmailAddress, String expectedPlaceholderPassword) {
        WebElement EmailAddress = driver.findElement(By.xpath("/html/body/div/div[2]/div/main/div[2]/form/div[1]/div/div/div/input"));
        String actualEmailPlaceholder = EmailAddress.getAttribute("placeholder");

        WebElement passwordField = driver.findElement(By.xpath("/html/body/div/div[2]/div/main/div[2]/form/div[2]/div/div/div/input"));
        String actualPasswordPlaceholder = passwordField.getAttribute("placeholder");

        assertEquals(expectedPlaceholderEmailAddress, actualEmailPlaceholder);
        assertEquals(expectedPlaceholderPassword, actualPasswordPlaceholder);

        allureScreenshot();
       //driver.quit();
    }

    //TC009
    @Then("page should show warning")
    public void pageShouldShowWarning() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\":r0:-helper-text\"]")
        ));
        WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\":r1:-helper-text\"]")
        ));
        assertEquals(email.getText(), "This field is required.");
        assertEquals(password.getText(), "This field is required.");

        allureScreenshot();

        //driver.quit();
    }

    //TC010
    @Then("email input box should show warning")
    public void emailInputBoxShouldShowWarning() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement warning = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\":r0:-helper-text\"]")
        ));
        assertEquals(warning.getText(), "This field is required.");

        allureScreenshot();

        //driver.quit();
    }

    //TC011
    @Then("password input box should show warning")
    public void passwordInputBoxShouldShowWarning() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement warning = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\":r1:-helper-text\"]")
        ));
        assertEquals(warning.getText(), "This field is required.");

        allureScreenshot();
        //driver.quit();
    }

    //TC013
    @Then("page should show email warning")
    public void pageShouldShowEmailWarning() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement warning = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\":r0:-helper-text\"]")
        ));
        assertEquals(warning.getText(), "Please provide a valid email");
        allureScreenshot();
        //driver.quit();
    }

    //TC014
    @Then("page should show credential warning")
    public void pageShouldShowCredentialWarning() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //Wait until the warning text is present in the <p> tag
        boolean isTextPresent = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.id(":r1:-helper-text"),
                "Invalid user credentials."
        ));

        //Retrieve the actual text to assert
        WebElement warning = driver.findElement(By.id(":r1:-helper-text"));
        String actualText = warning.getText().trim();
        System.out.println("Warning Text: '" + actualText + "'");

        assertEquals(actualText, "Invalid user credentials.");
        allureScreenshot();
        //driver.quit();
    }

    //TC018
    @When("user click login repeatedly")
    public void userClickLoginRepeatedly() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        for (int i = 0; i < 5; i++) {
            try {
                WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[p[text()='Login']]")
                ));
                loginButton.click();
                Thread.sleep(1);
            } catch (StaleElementReferenceException | TimeoutException | InterruptedException e) {
                System.out.println("Attempt " + (i + 1) + ": Failed to click due to stale or missing element.");
            }
        }
        //driver.quit();
    }


}
