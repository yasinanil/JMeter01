package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "html:target/reports/html-report.html",
                "pretty"
        },
        features = "src/test/resources/features", // Path to the feature files
        glue = "stepdefinitions",                 // Path to the step definitions package
        tags = "@Delete",                       // Tags to specify which scenarios to run
        dryRun = false                             // Set to true to verify if all steps are defined
)
public class DeleteUnregisteredUsers {
    // No additional code is required in the runner class
}