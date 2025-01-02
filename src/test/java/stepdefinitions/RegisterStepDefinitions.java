package stepdefinitions;

import com.github.javafaker.Faker;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import pages.LoginPage;
import pages.SignUpPage;
import utilities.ConfigReader;
import utilities.Driver;
import utilities.JDBCUtils;
import utilities.JMeterUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static base_urls.BazaarStoreBaseUrl.spec;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class RegisterStepDefinitions {

    LoginPage loginPage;
    SignUpPage signUpPage;
    String name;
    public static String email;

    @Given("go to homepage")
    public void go_to_homepage() {
        Driver.getDriver().get(ConfigReader.getProperty("url"));
    }

    @When("click on Sign up button")
    public void clickOnSignUpButton() {
        loginPage = new LoginPage();
        loginPage.signUp.click();
    }

    @And("Enter valid values for all fields")
    public void enterValidValuesForAllFields() {
        signUpPage = new SignUpPage();
        name = Faker.instance().name().fullName();
        email = Faker.instance().internet().emailAddress();
        signUpPage.name.sendKeys(name);
        signUpPage.email.sendKeys(email);
        signUpPage.password.sendKeys("password");
        signUpPage.password_confirmation.sendKeys("password");
    }


    @When("Submit the form")
    public void submit_the_form() {
        signUpPage.signUpButton.click();
    }

    @Then("Assert the success message")
    public void assert_the_success_message() {
        String successMessage = signUpPage.successMessage.getText();
        assert successMessage.contains("Your registration has been successfully completed");
    }

    @Then("Validate user via API")
    public void validate_user_via_api() {

        Response response = given(spec)
                .get("/api/users");

        //response.prettyPrint();

        Object actualName = response.jsonPath().getList("findAll{it.email=='" + email + "'}.name").get(0);
        Object actualEmail = response.jsonPath().getList("findAll{it.email=='" + email + "'}.email").get(0);
        Object actualRole = response.jsonPath().getList("findAll{it.email=='" + email + "'}.role").get(0);

        assertEquals(name, actualName);
        assertEquals(email, actualEmail);
        assertEquals("customer", actualRole);

    }

    @Then("Validate user via DB")
    public void validate_user_via_db() throws SQLException {
        ResultSet resultSet = JDBCUtils.getRecordByEmail(email);

        String actualName = resultSet.getString("name");
        String actualEmail = resultSet.getString("email");
        String actualRole = resultSet.getString("role");

        assertEquals(name, actualName);
        assertEquals(email, actualEmail);
        assertEquals("customer", actualRole);

    }

    @And("Close the browser")
    public void closeTheBrowser() {
        Driver.closeDriver();
    }


    @Given("delete")
    public void delete() {
        Response response = given(spec).get("/api/users");
        List<Integer> nullIds = response.jsonPath().getList("findAll{it.email_verified_at==null}.id");
        System.out.println("nullIds = " + nullIds);
        for (int id : nullIds) {
            given(spec).delete("api/users/" + id);
        }
    }

    @Given("test if {int} people can register at the same time")
    public void testIfPeopleCanRegisterAtTheSameTime(int numberOfPeople)  {

        JMeterUtils.RunJMX("JMeterApiTest");

    }


    }
