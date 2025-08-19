package steps;

import drivers.DriverInstance;
import hooks.Hooks;
import io.cucumber.java.en.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import qa.util.ExcelReader;
import qa.util.ExternalFunction;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

public class AdminChangePasswordSteps extends DriverInstance {

    private String mainWindowHandle;
    private String retrievedOTP;

    WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));

    //IHP-61-001
    @Given("User logs in to the HR portal using {string} and {string} credentials")
    public void user_logs_in_to_the_hr_portal_using_and_credentials(String email, String password) {
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

        assertTrue("Admin dashboard element not visible.", dashboardElement.isDisplayed());

        System.out.println("Admin dashboard loaded successfully.");

        allureScreenshot();
    }

    @Given("User navigate to profile")
    public void user_navigate_to_profile() {

        ExternalFunction.waitForLoaderToDisappear(driver);

        WebElement profileButton = longWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"layout-container\"]/header/div/div/div[2]/img")));
        profileButton.click();
    }

    @When("User click change password")
    public void user_click_change_password() throws InterruptedException {

        ExternalFunction.waitForLoaderToDisappear(driver);

        WebElement changePassword = longWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(),'Change Password')]")));
        //Thread.sleep(1000);
        changePassword.click();

    }

    @Then("Change password tab should be shown")
    public void change_password_tab_should_be_shown() {
        WebElement passwordTab = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[3]/div[3]/div[1]/p")
        ));

        assertTrue("Change password tab not visible.", passwordTab.isDisplayed());

        System.out.println("Change password tab loaded successfully.");

        allureScreenshot();
    }

    //IHP-61-002
    @And("User enter current password {string}")
    public void userEnterCurrentPassword(String currentPassword) throws InterruptedException {
        ExternalFunction.waitForLoaderToDisappear(driver);
        WebElement curPasswordField = longWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("currentPassword")));
        curPasswordField.clear();
        if (!currentPassword.isEmpty()) {
            curPasswordField.sendKeys(currentPassword);
        }

    }

    //IHP-61-003
    @And("User enter valid new password {string}")
    public void userEnterValidNewPassword(String newPassword) throws InterruptedException {
        ExternalFunction.waitForLoaderToDisappear(driver);
        WebElement newPasswordField = longWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newPassword")));
        newPasswordField.clear();
        if (!newPassword.isEmpty()) {
            newPasswordField.sendKeys(newPassword);
        }
    }

    @And("User confirm new password {string}")
    public void userConfirmNewPassword(String confirmNewPassword) throws InterruptedException {
        ExternalFunction.waitForLoaderToDisappear(driver);
        WebElement confPasswordField = longWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("confirmPassword")));
        confPasswordField.clear();
        if (!confirmNewPassword.isEmpty()) {
            confPasswordField.sendKeys(confirmNewPassword);
        }
    }

    @When("User click change password button")
    public void userClickChangePasswordButton() throws InterruptedException {

        ExternalFunction.waitForLoaderToDisappear(driver);
        WebElement changePasswordBtn = longWait.until(

                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[.//p[text()='Change Password']]")
                )
        );

        changePasswordBtn.click();

        allureScreenshot();

    }

    @Then("OTP tab should display")
    public void otpTabShouldDisplay() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.otp-modal")
        ));

        assertTrue("OTP Modal should be displayed", modal.isDisplayed());

        WebElement modalTitle = driver.findElement(By.cssSelector("p.otp-modal__title"));
        assertEquals(modalTitle.getText(), "OTP Verification");

        allureScreenshot();
    }

    @And("User open email with {string} and {string}")
    public void userOpenEmailWithEmailUsernameAndEmailPassword(String emailUsername, String emailPassword) throws InterruptedException {
        // Make sure driver and waits are initialized
        if (driver == null) {
            driver = Hooks.driver;
        }
        if (driver == null) {
            throw new IllegalStateException("WebDriver is null! Make sure Hooks sets it before this step.");
        }
        if (wait == null) {
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        }
        if (longWait == null) {
            longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        }

        // Store the current window handle
        mainWindowHandle = driver.getWindowHandle();

        // Open a new tab
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://mailsac.com/login");
        Thread.sleep(2000);

        // Login
        longWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys(emailUsername);
        driver.findElement(By.name("password")).sendKeys(emailPassword);

        WebElement signInButton = longWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[1]/div[2]/div[1]/div/div/div/form/button")));
        signInButton.click();

        // Open email
        WebElement email = longWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[1]/div[1]/div/div/div/div/div[2]/div[3]/div[1]/table[1]/tbody/tr[2]/td/a")));
        email.click();
        allureScreenshot();
    }

    @And("User retrieve OTP")
    public void userRetrieveOTP() {
        // Wait for latest OTP email
        longWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".row")));
        driver.findElements(By.cssSelector(".row")).get(0).click();

        allureScreenshot();

        // Wait for message body
        WebElement messageBody = longWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[2]/div[1]/div/div[2]/div/table/tbody/tr[2]/td[2]/div[2]/div[2]/table/tbody/tr[5]/td/b")));

        allureScreenshot();

        // Extract OTP using regex (assumes 6 digits)
        String bodyText = messageBody.getText();
        Pattern otpPattern = Pattern.compile("\\b\\d{6}\\b");
        Matcher matcher = otpPattern.matcher(bodyText);

        if (matcher.find()) {
            retrievedOTP = matcher.group();
            System.out.println("OTP Retrieved: " + retrievedOTP);
        } else {
            throw new RuntimeException("OTP not found in email.");
        }

        // Close email tab and switch back to portal
        driver.close();
        driver.switchTo().window(mainWindowHandle);
    }

    @And("User filled the OTP")
    public void userFilledTheOTP() {
        String otp = retrievedOTP;

        for (int i = 0; i < otp.length(); i++) {
            WebElement otpField = driver.findElement(By.name("otp-" + i));
            otpField.clear();
            otpField.sendKeys(Character.toString(otp.charAt(i)));
        }

        allureScreenshot();
    }

    @And("User successfully change password")
    public void userSuccessfullyChangePassword() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement doneButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='successful-modal__button-container']//button")
        ));
        allureScreenshot();

        doneButton.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.className("successful-modal__button-container")
        ));
        allureScreenshot();
    }

    //IHP-61-004
    @And("User click Eye Icon")
    public void userClickEyeIcon() throws InterruptedException {

        ExternalFunction.waitForLoaderToDisappear(driver);

        // Current password eye icon
        WebElement currentPassEye = longWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='currentPassword']/following-sibling::div/button[@class='app-input__password-toggle']")));
        currentPassEye.click();

        // New password eye icon
        WebElement newPassEye = longWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='newPassword']/following-sibling::div/button[@class='app-input__password-toggle']")));
        newPassEye.click();

        // Confirm password eye icon
        WebElement confirmPassEye = longWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='confirmPassword']/following-sibling::div/button[@class='app-input__password-toggle']")));
        confirmPassEye.click();

        Thread.sleep(5000);

        allureScreenshot();
    }

    //IHP-61-005
    @When("User click Cancel button")
    public void userClickCancelButton() throws InterruptedException {

        ExternalFunction.waitForLoaderToDisappear(driver);

        WebElement cancelButton = longWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[3]/div[2]/form/div[4]/button[1]")));
        cancelButton.click();
        Thread.sleep(5000);
        allureScreenshot();

    }

    //IHP-61-006 , 007 , 008, 009, 010, 011,
    @Then("The password rule {string} should be {string}")
    public void thePasswordRuleShouldBe(String ruleTextValue, String expectedState ) {
        WebElement ruleText = driver.findElement(
                By.xpath("//ul[@class='update-password__rules']//span[contains(normalize-space(), '" + ruleTextValue + "')]")
        );

        String ruleClass = ruleText.getAttribute("class").trim();

        boolean isActive = ruleClass.contains("text-green");
        //allureScreenshot();
        boolean isInactive = ruleClass.contains("text-gray");
        //allureScreenshot();

        switch (expectedState.toLowerCase()) {
            case "active":
                if (!isActive) {
                    throw new AssertionError("Expected rule '" + ruleTextValue + "' to be ACTIVE but found class: " + ruleClass);
                }
                //allureScreenshot();
                break;
            case "inactive":
                if (!isInactive) {
                    throw new AssertionError("Expected rule '" + ruleTextValue + "' to be INACTIVE but found class: " + ruleClass);
                }
                //allureScreenshot();
                break;
            default:
                throw new IllegalArgumentException("Unknown expectedState: " + expectedState);
        }

        allureScreenshot();
    }

    @Then("Tab should be close without saving changes")
    public void tabShouldBeCloseWithoutSavingChanges() throws InterruptedException {
        //ExternalFunction.waitForLoaderToDisappear(driver);

        WebElement profileButton = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[1]/div[2]/main/div[2]/header/div/div/div[2]/img")));
        profileButton.click();

        WebElement changePassword = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(),'Change Password')]")));
        //Thread.sleep(1000);
        changePassword.click();

        allureScreenshot();
    }

    @Then("The Change Password button should be disabled")
    public void theChangePasswordButtonShouldBeDisabled() {
        WebElement changePasswordButton = driver.findElement(By.xpath("//button[.//p[text()='Change Password']]"));
        boolean isDisabled = changePasswordButton.getAttribute("disabled") != null;

        assertTrue("Expected Change Password button to be disabled, but it was enabled.", isDisabled);

        allureScreenshot();
    }

    @But("New password equal to current password")
    public void newPasswordEqualToCurrentPassword() {

        ExternalFunction.waitForLoaderToDisappear(driver);

        WebElement currentPasswordField = driver.findElement(By.name("currentPassword"));
        String currentPasswordValue = currentPasswordField.getAttribute("value");

        WebElement newPasswordField = driver.findElement(By.name("newPassword"));
        newPasswordField.clear();
        newPasswordField.sendKeys(currentPasswordValue);

        allureScreenshot();
    }

    @Then("Validation message should remain gray with exclamation icon")
    public void validationMessageShouldRemainGrayWithExclamationIcon() {

        WebElement validationMessage = longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[@class='update-password__rules__maps']//span[text()='Cannot be same as previous password']")
        ));

        String classAttribute = validationMessage.getAttribute("class");
        assertTrue("Expected the text to remain gray", classAttribute.contains("text-gray"));

        WebElement icon = validationMessage.findElement(By.xpath("./preceding-sibling::img[@alt='exclamationIcon']"));
        assertNotNull("Expected exclamation icon to be present", icon);

        allureScreenshot();
    }

    @Then("All password rules should be:")
    public void allPasswordRulesShouldBe(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rules = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rules) {
            String ruleTextValue = row.get("Rule Text");
            String expectedState = row.get("Expected State").toLowerCase();

            WebElement ruleText = driver.findElement(
                    By.xpath("//ul[@class='update-password__rules']//span[contains(normalize-space(), '" + ruleTextValue + "')]")
            );

            String ruleClass = ruleText.getAttribute("class").trim();
            boolean isActive = ruleClass.contains("text-green");
            boolean isInactive = ruleClass.contains("text-gray");

            switch (expectedState) {
                case "active":
                    if (!isActive) {
                        throw new AssertionError("Expected '" + ruleTextValue + "' to be ACTIVE but found: " + ruleClass);
                    }
                    break;
                case "inactive":
                    if (!isInactive) {
                        throw new AssertionError("Expected '" + ruleTextValue + "' to be INACTIVE but found: " + ruleClass);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Expected State: " + expectedState);
            }

        }
        allureScreenshot();
    }

    @But("{string} does not meet the rules")
    public void newPasswordDoesNotMeetTheRules(String newPassword) {
        ExternalFunction.waitForLoaderToDisappear(driver);
        WebElement newPasswordField = longWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newPassword")));
        newPasswordField.clear();
        if (!newPassword.isEmpty()) {
            newPasswordField.sendKeys(newPassword);
        }
    }

    @Then("User press Enter key")
    public void userPressEnterKey() {
        WebElement confirmNewPassword = driver.findElement(By.name("confirmPassword"));
        confirmNewPassword.sendKeys(Keys.ENTER);
        allureScreenshot();
    }

    @Then("The new password field should contain only the first twenty characters")
    public void theNewPasswordFieldShouldContainOnlyTheFirstTwentyCharacters() {
        ExternalFunction.waitForLoaderToDisappear(driver);

        WebElement newPasswordField = driver.findElement(By.name("newPassword"));

        String actualValue = newPasswordField.getAttribute("value");

        String expectedValue = actualValue.length() > 20
                ? actualValue.substring(0, 20)
                : actualValue;


        assertEquals(actualValue.length(), 20,
                "Password field contains more than 20 characters");

        allureScreenshot();
    }

    @And("The form should display warning")
    public void theFormShouldDisplayWarning() {
        ExternalFunction.waitForLoaderToDisappear(driver);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(@class,'Mui-error') and text()='Password should not contain spaces']")
        ));
        assertEquals(errorMessage.getText(), "Password should not contain spaces");
        allureScreenshot();
    }

    @And("User failed to change password")
    public void userFailedToChangePassword() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[text()='Password Incorrect']")
        ));

        assertTrue("Expected 'Password Incorrect' message to be displayed", errorMsg.isDisplayed());

        assertEquals(errorMsg.getText(), "Password Incorrect");

        allureScreenshot();


    }
}


