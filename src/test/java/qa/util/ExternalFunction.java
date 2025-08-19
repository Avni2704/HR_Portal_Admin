package qa.util;

import drivers.DriverInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExternalFunction extends DriverInstance {
    public static void waitForLoaderToDisappear(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(120)).until(ExpectedConditions.invisibilityOfElementLocated(
                By.className("screen-loading")
        ));
    }
}
