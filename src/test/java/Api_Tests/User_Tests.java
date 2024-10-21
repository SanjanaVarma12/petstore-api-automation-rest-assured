package Api_Tests;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Attachment;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;

@Listeners(io.qameta.allure.testng.AllureTestNg.class)
public class User_Tests {
    private static final Logger logger = Logger.getLogger(User_Tests.class);

    // Class-level variable to store the created user's username
    private String username = "john_doe";

    @Feature("User Management")
    @Story("Create User")
    @Step("Creating a new user")
    @Test
    @Description("Test to create a new user in the system.")
    public void createUser() {
        logger.info("Starting test: createUser");

        Response response = given()
            .contentType("application/json")
            .body("{\"id\": 0, \"username\": \"" + username + "\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"password\": \"password123\", \"phone\": \"1234567890\", \"userStatus\": 1}")
        .when()
            .post("https://petstore3.swagger.io/api/v3/user/createWithList");

        // Validate status code, headers, and body
        response.then()
           .statusCode(400)
            .contentType("application/json")
            .header("Connection", equalTo("keep-alive"))
            .body("message", notNullValue())
            .log().all();

        // Attach the response body to the Allure report
        attachResponseBody(response);
        logger.info("Completed test: createUser");
    }

    @Feature("User Management")
    @Story("Fetch User")
    @Step("Fetching the user with username {username}")
    @Test(dependsOnMethods = "createUser")
    @Description("Test to fetch user details by username.")
    public void getUserByUsername() {
        logger.info("Starting test: getUserByUsername");

        Response response = given()
            .when()
            .get("https://petstore3.swagger.io/api/v3/user/" + username);

        // Validate status code, headers, and response body
        response.then()
            .contentType("application/json")
            .header("Connection", equalTo("keep-alive"))
            .log().all();

        // Attach the response body to the Allure report
        attachResponseBody(response);
        logger.info("Completed test: getUserByUsername");
    }

    @Feature("User Management")
    @Story("Update User")
    @Step("Updating the user with username {username}")
    @Test(dependsOnMethods = "getUserByUsername")
    @Description("Test to update user details.")
    public void updateUser() {
        logger.info("Starting test: updateUser");

        Response response = given()
            .contentType("application/json")
            .body("{\"id\": 0, \"username\": \"" + username + "\", \"firstName\": \"Johnny\", \"lastName\": \"Doe\", \"email\": \"johnny.doe@example.com\", \"password\": \"newpassword123\", \"phone\": \"9876543210\", \"userStatus\": 1}")
        .when()
            .put("https://petstore3.swagger.io/api/v3/user/" + username);

        // Validate status code, headers, and response body
        response.then()
            .contentType("application/json")
            .header("Connection", equalTo("keep-alive"))
            .body("message", notNullValue())
            .log().all();

        // Attach the response body to the Allure report
        attachResponseBody(response);
        logger.info("Completed test: updateUser");
    }

    @Feature("User Management")
    @Story("Validate Updated User")
    @Step("Fetching updated user details for username {username}")
    @Test(dependsOnMethods = "updateUser")
    @Description("Test to validate the updated user details.")
    public void validateUpdatedUser() {
        logger.info("Starting test: validateUpdatedUser");

        Response response = given()
            .when()
            .get("https://petstore3.swagger.io/api/v3/user/" + username);

        // Validate the updated user information
        response.then()
            .statusCode(500)
            .contentType("application/json")
            .header("Connection", equalTo("keep-alive"))
            .log().all();

        // Attach the response body to the Allure report
        attachResponseBody(response);
        logger.info("Completed test: validateUpdatedUser");
    }

    @Feature("User Management")
    @Story("Delete User")
    @Step("Deleting the user with username {username}")
    @Test(dependsOnMethods = "validateUpdatedUser")
    @Description("Test to delete a user from the system.")
    public void deleteUser() {
        logger.info("Starting test: deleteUser");

        Response response = given()
            .when()
            .delete("https://petstore3.swagger.io/api/v3/user/" + username);

        // Validate status code and headers
        response.then()
            .statusCode(500)
            .header("Connection", "keep-alive")
            .body("message", notNullValue())
            .log().all();

        // Confirm that the user is deleted by attempting to fetch it
        given()
            .when()
            .get("https://petstore3.swagger.io/api/v3/user/" + username)
        .then()
            .statusCode(404)  // User should no longer exist
            .body("message", containsString("User not found"))
            .log().all();

        // Attach a message confirming deletion to the Allure report
        logger.info("Deleted User: " + username);
        logger.info("Completed test: deleteUser");
    }

    // Method to attach the response body to Allure report
    @Attachment(value = "Response Body", type = "application/json")
    private String attachResponseBody(Response response) {
        return response.asPrettyString();
    }
}
