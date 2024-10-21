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
public class Store_Tests {
    private static final Logger logger = Logger.getLogger(Store_Tests.class);

    // Class-level variable to store the created order ID
    private long orderId;

    @Test
    @Feature("Store Inventory")
    @Story("Get Inventory Status")
    @Description("Test to get the inventory status of pets.")
    public void getInventory() {
        logger.info("Starting test: getInventory");

        Response response = given()
            .when()
            .get("https://petstore3.swagger.io/api/v3/store/inventory");

        // Validate status code, headers, and response body
        response.then()
             .statusCode(200)
            .contentType("application/json")
            .header("Connection", equalTo("keep-alive"))
            .log().all();

        // Attach the response body to the Allure report
        attachResponseBody(response);
        logger.info("Completed test: getInventory");
    }

    @Step("Placing a new order for a pet")
    @Test
    @Feature("Store Orders")
    @Story("Place New Order")
    @Description("Test to place an order in the store.")
    public void placeOrder() {
        logger.info("Starting test: placeOrder");

        Response response = given()
            .contentType("application/json")
            .body("{\"id\": 0, \"petId\": 10, \"quantity\": 1, \"shipDate\": \"2024-10-18T15:43:00.000Z\", \"status\": \"placed\", \"complete\": true}")
        .when()
            .post("https://petstore3.swagger.io/api/v3/store/order");

        // Extract the 'id' field from the response and store it in orderId
        orderId = response.jsonPath().getLong("id");

        // Validate status code, headers, and response body
        response.then()
            .statusCode(200)
            .contentType("application/json")
            .header("Connection", equalTo("keep-alive"))
            .header("Content-Type", containsString("application/json"))
            .body("id", notNullValue())
            .body("quantity", equalTo(1))
            .body("shipDate", notNullValue())
            .body("status", equalTo("placed"))
            .body("complete", equalTo(true))
            .log().all();

        // Attach the response body and order ID to the Allure report
        attachResponseBody(response);
        logger.info("Created Order ID: " + orderId);
        logger.info("Completed test: placeOrder");
    }

    @Step("Fetching the order with ID {orderId}")
    @Test(dependsOnMethods = "placeOrder")
    @Feature("Store Orders")
    @Story("Fetch Order by ID")
    @Description("Test to fetch an order by ID and validate details.")
    public void getOrderById() {
        logger.info("Starting test: getOrderById");

        Response response = given()
            .when()
            .get("https://petstore3.swagger.io/api/v3/store/order/" + orderId);

        // Validate status code, headers, and response body
        response.then()
            .statusCode(200)
            .contentType("application/json")
            .header("Connection", equalTo("keep-alive"))
            .body("id", equalTo((int) orderId))
            .body("petId", equalTo(10))
            .body("quantity", equalTo(1))
            .body("status", equalTo("placed"))
            .body("complete", equalTo(true))
            .log().all();

        // Attach the response body to the Allure report
        attachResponseBody(response);
        logger.info("Completed test: getOrderById");
    }

    @Step("Deleting the order with ID {orderId}")
    @Test(dependsOnMethods = "getOrderById")
    @Feature("Store Orders")
    @Story("Delete Order")
    @Description("Test to delete an order and validate its removal.")
    public void deleteOrder() {
        logger.info("Starting test: deleteOrder");

        Response response = given()
            .when()
            .delete("https://petstore3.swagger.io/api/v3/store/order/" + orderId);

        // Validate status code and headers
        response.then()
            .statusCode(200)
            .header("Connection", "keep-alive")
            .header("Content-Length", equalTo("0"))
            .log().all();

        // Verify the order is deleted by trying to retrieve it
        given()
            .when()
            .get("https://petstore3.swagger.io/api/v3/store/order/" + orderId)
        .then()
            .statusCode(404)  // Order should no longer exist
            .log().all();

        // Attach a message confirming deletion to the Allure report
        logger.info("Deleted Order ID: " + orderId);
        logger.info("Completed test: deleteOrder");
    }

    // Method to attach the response body to Allure report
    @Attachment(value = "Response Body", type = "application/json")
    private String attachResponseBody(Response response) {
        return response.asPrettyString();
    }
}
