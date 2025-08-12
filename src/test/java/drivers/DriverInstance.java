package drivers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.qameta.allure.Allure;
import qa.util.Variables;


public class DriverInstance extends Variables {

    public static RemoteWebDriver driver;
    public static WebDriverWait wait;

    public static String module;
    public static String subModule;

    public void allureScreenshot() {
        byte[] screenshot1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment("Step Screenshot", "image/png", new ByteArrayInputStream(screenshot1), "png");
    }

    public void scrollDown() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("window.scrollBy(0, 500);");
    }
    public void scrollRight() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Scroll right by 500 pixels horizontally
        js.executeScript("window.scrollBy(500, 0);");
    }

    public void scrollUp() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("window.scrollBy(0, -500);");
    }

    public static String extractCodeFromStatement(String statement) {
        // Define a regular expression pattern to match the code format
        Pattern pattern = Pattern.compile("UAT-GI-Q\\d{8}");
        Matcher matcher = pattern.matcher(statement);

        if (matcher.find()) {
            return matcher.group(); // Returns the matched code
        }
        return null; // No matching code found
    }
}
