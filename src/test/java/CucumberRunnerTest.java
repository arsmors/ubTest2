import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "steps",
        plugin = {
                "json:target/cucumber-report.json",
                "html:target/cucumber-report.html"
        })

public class CucumberRunnerTest extends AbstractTestNGCucumberTests {

}