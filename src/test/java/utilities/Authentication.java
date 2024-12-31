package utilities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static stepdefinitions.RegisterStepDefinitions.email;

public class Authentication {

    public static String getToken() {

        Map payload = new HashMap();
        payload.put("email", "customer@novatechacademy.com");
        payload.put("password", "password");

        Response response = given().contentType(ContentType.JSON).body(payload).post(ConfigReader.getProperty("url") + "/api/login");
        return response.jsonPath().getString("authorisation.token");

    }


}
