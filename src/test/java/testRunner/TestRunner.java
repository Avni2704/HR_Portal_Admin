package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        //features = {"src/test/java/features/AdminChangePassword.feature"},
        features = {"src/test/java/features/AdminLogin.feature"},
        //dryRun = true,
        glue = { "steps", "hooks" },
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        monochrome = true, // TO REMOVE JUNK CHARACTERS
        //tags = "@TC002",
        plugin = { "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" })

public class TestRunner extends AbstractTestNGCucumberTests {
}
