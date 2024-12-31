package base_urls;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static utilities.Authentication.getToken;

public class BazaarStoreBaseUrl {

    public static RequestSpecification spec;

    static {
        spec = new RequestSpecBuilder()
                .setBaseUri("https://bazaarstores.com")
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer "+getToken())
                .build();
    }


}
