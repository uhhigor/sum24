package org.example.api.e2e.steps;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class UserSteps {

    final String  BASIC_URL = "http://localhost:8080";


    public String userLogsIn(String username, String password) {
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(String.format("""
                        {
                          "id": 0,
                          "username": "%s",
                          "password": "%s",
                          "role": "ADMIN"
                        }""", username, password))
                .post(BASIC_URL + "/login");

        assertEquals(200, response.getStatusCode(), "User should be able to login with correct credentials");

        String token = response.jsonPath().getString("token");
        assertNotNull(token, "Token should not be null after login");
        return token;
    }

    public void userCanRegister(String username, String password) {
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(String.format("""
                        {
                          "id": 0,
                          "username": "%s",
                          "password": "%s",
                          "role": "ADMIN"
                        }""", username, password))
                .post(BASIC_URL + "/register");

        assertEquals(200, response.getStatusCode(), "User should be able to register");

        String token = response.jsonPath().getString("token");
        assertNotNull(token, "Token should be null after registration");
    }

    public String getUserId(String username) {
        Response response = RestAssured.given()
                .contentType("application/json")
                .get(BASIC_URL + "/login/" + username);
        assertEquals(200, response.getStatusCode(), "User should be able to get their id");

        String userId = response.asString();
        assertNotNull(String.valueOf(userId), "User id should not be null");

        return userId;
    }

    public void deleteUser(String username) {
        RestAssured.given()
                .contentType("application/json")
                .delete(BASIC_URL + "/user/" + username);
    }

}
