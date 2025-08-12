package hooks;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;

import drivers.DriverInstance;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

import static drivers.DriverInstance.driver;

public class Hooks extends DriverInstance  {
    @BeforeStep
    public void beforeSteps(Scenario scenario) {

    }

    @AfterStep
    public void afterSteps(Scenario scenario) {
        byte[] screenshotAs = driver.getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshotAs, "image/png" , "Screenshot");
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("Hello Portal");
        driver = new ChromeDriver();

        String scenName = scenario.getName();
        System.out.println("Scenario Name: " + scenName);
    }

    @After
    public void afterScenario(Scenario scenario) {
        boolean failed = scenario.isFailed();
        System.out.println("Is Failed: " + failed);

        if (failed) {
            byte[] screenshotAs = driver.getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshotAs, "image/png", "Error Screenshot");
        } else {
            byte[] screenshotAs = driver.getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshotAs, "image/png", "Page Screenshot");
        }

        System.out.println("============================================================================");
        driver.close();
    }

}
