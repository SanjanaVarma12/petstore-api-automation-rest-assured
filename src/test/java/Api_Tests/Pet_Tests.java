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
import io.qameta.allure.Feature; // Importing Feature annotation
import io.qameta.allure.Story; // Importing Story annotation

@Listeners(io.qameta.allure.testng.AllureTestNg.class)
public class Pet_Tests {
    private static final Logger logger = Logger.getLogger(Pet_Tests.class);

    // Class-level variable to store the created pet's ID
    private long petId;

    @Feature("Pet Management") // Adding Feature annotation
    @Story("Retrieve Available Pets") // Adding Story annotation
    @Test
    @Description("Test to get a list of pets with status 'available' from the API.")
    public void getPets() {
        logger.info("Starting test: getPets");
        
        Response response = given()
            .when()
            .get("https://petstore3.swagger.io/api/v3/pet/findByStatus?status=available");

        // Validate status code, content type, and headers
        response.then()
        .statusCode(200) // Validate the status code
        .contentType("application/json") // Validate the content type
        .header("Access-Control-Allow-Origin", "*") // Validate Access-Control-Allow-Origin header
        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT") // Validate Access-Control-Allow-Methods header
        .header("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization") // Validate Access-Control-Allow-Headers header
        .header("Access-Control-Expose-Headers", "Content-Disposition") // Validate Access-Control-Expose-Headers header
        .header("Server", "Jetty(9.4.53.v20231009)") // Validate the Server header
        .log().all();

        // Attach the response body to the Allure report
        attachResponseBody(response);
        logger.info("Completed test: getPets");
    }

    @Feature("Pet Management") // Adding Feature annotation
    @Story("Create New Pet") // Adding Story annotation
    @Step("Creating a new pet")
    @Test
    @Description("Test to create a new pet in the API with detailed validations.")
    public void createPet() {
        logger.info("Starting test: createPet");

        Response response = given()
            .contentType("application/json")
            .body("{\"id\": 0, \"name\": \"Buddy\", \"category\": {\"id\": 1, \"name\": \"Dogs\"}, \"status\": \"available\"}")
            .when()
            .post("https://petstore3.swagger.io/api/v3/pet");

        // Extract the 'id' field from the response and store it in petId
        petId = response.jsonPath().getLong("id");

        // Validate status code, headers, and response body
        response.then()
            .statusCode(200)
            .contentType("application/json") // Validate that the content type is JSON
            .header("Access-Control-Allow-Origin", "*") // Validate Access-Control-Allow-Origin header
            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT") // Validate Access-Control-Allow-Methods header
            .header("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization") // Validate Access-Control-Allow-Headers
            .header("Access-Control-Expose-Headers", "Content-Disposition") // Validate Access-Control-Expose-Headers
            .header("Server", "Jetty(9.4.53.v20231009)") // Validate the Server header
            .log().all();

        // Attach the response body and pet ID to the Allure report
        attachResponseBody(response);
        logger.info("Created Pet ID: " + petId);
        logger.info("Completed test: createPet");
    }

    @Feature("Pet Management") // Adding Feature annotation
    @Story("Update Existing Pet") // Adding Story annotation
    @Step("Updating the pet with ID {petId}")
    @Test(dependsOnMethods = "createPet")
    @Description("Test to update an existing pet with additional validations.")
    public void updatePet() {
        logger.info("Starting test: updatePet");
        
        Response response = given()
            .contentType("application/json")
            .body("{\"id\": " + petId + ", \"name\": \"Max\", \"status\": \"sold\", \"category\": {\"id\": 1, \"name\": \"Dogs\"}}")
            .when()
            .put("https://petstore3.swagger.io/api/v3/pet");

        // Validate status code, headers, and response body
        response.then()
            .statusCode(200)
            .contentType("application/json") // Validate the Content-Type header
            .header("Connection", "keep-alive") // Validate the Connection header
            .header("Access-Control-Allow-Origin", "*") // Validate CORS header
            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT") // Validate allowed methods
            .header("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization") // Validate allowed headers
            .header("Access-Control-Expose-Headers", "Content-Disposition") // Validate exposed headers
            .header("Server", "Jetty(9.4.53.v20231009)") // Validate the Server header
            .body("id", equalTo(0)) // Validate id is 0
            .body("category.id", equalTo(1)) // Validate category id is 1
            .body("category.name", equalTo("Dogs")) // Validate category name is "Dogs"
            .body("name", equalTo("Max")) // Validate the name is "Max"
            .body("photoUrls", hasSize(0)) // Validate photoUrls is an empty array
            .body("tags", hasSize(0)) // Validate tags is an empty array
            .body("status", equalTo("sold")) // Validate status is "sold"
            .log().all();

        // Attach the response body to the Allure report
        attachResponseBody(response);
        logger.info("Completed test: updatePet");
    }

    @Feature("Pet Management") // Adding Feature annotation
    @Story("Delete Pet") // Adding Story annotation
    @Step("Deleting the pet with ID {petId}")
    @Test(dependsOnMethods = "updatePet")
    @Description("Test to delete a pet from the API and verify.")
    public void deletePet() {
        logger.info("Starting test: deletePet");

        Response response = given()
            .when()
            .delete("https://petstore3.swagger.io/api/v3/pet/" + petId);

        // Validate status code and headers
        response.then()
            .statusCode(200)
            .header("Connection", "keep-alive")
            .contentType("application/json") // Validate the content type
            .header("Access-Control-Allow-Origin", "*") // Validate Access-Control-Allow-Origin header
            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT") // Validate Access-Control-Allow-Methods header
            .header("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization") // Validate Access-Control-Allow-Headers header
            .header("Access-Control-Expose-Headers", "Content-Disposition") // Validate Access-Control-Expose-Headers header
            .header("Server", "Jetty(9.4.53.v20231009)") // Validate the Server header
            .log().all();

        // Verify the pet is deleted by trying to retrieve it
        given()
            .when()
            .get("https://petstore3.swagger.io/api/v3/pet/" + petId)
            .then()
            .statusCode(404)  // Pet should no longer exist
            .contentType("application/json") // Validate the content type
            .header("Access-Control-Allow-Origin", "*") // Validate Access-Control-Allow-Origin header
            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT") // Validate Access-Control-Allow-Methods header
            .header("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization") // Validate Access-Control-Allow-Headers header
            .header("Access-Control-Expose-Headers", "Content-Disposition") // Validate Access-Control-Expose-Headers header
            .log().all();

        // Attach a message confirming deletion to the Allure report
        logger.info("Deleted Pet ID: " + petId);
        logger.info("Completed test: deletePet");
    }

    // Method to attach the response body to Allure report
    @Attachment(value = "Response Body", type = "application/json")
    private String attachResponseBody(Response response) {
        return response.asPrettyString();
    }
}
