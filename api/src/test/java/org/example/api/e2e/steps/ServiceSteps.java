package org.example.api.e2e.steps;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ServiceSteps {
    public int userAddsService(String userId, String name, String address) {
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(String.format("""
                        {
                          "name": "%s",
                          "address": "%s",
                          "fields": []
                        }""", name, address))
                .post("http://localhost:8080/service/user/" + userId);

        assertEquals(200, response.getStatusCode(), "User should be able to add service");
        int serviceId = response.jsonPath().getInt("service.id");
        assertNotNull(String.valueOf(serviceId), "Service id should not be null");
        assertEquals(name, response.jsonPath().getString("service.name"));
        assertEquals(address, response.jsonPath().getString("service.address"));
        return serviceId;
    }

    public void userDeletesService(int serviceId) {
        Response response = RestAssured.given()
                .contentType("application/json")
                .delete("http://localhost:8080/service/" + serviceId);

        assertEquals(200, response.getStatusCode(), "User should be able to delete service");
    }

    public void userUpdatesService(int serviceId, String token) {
        Response response = RestAssured.given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body("""
                        {
                          "name": "newName",
                          "address": "newAddress"
                        }""")
                .put("http://localhost:8080/service/" + serviceId);
        assertEquals(200, response.getStatusCode(), "User should be able to update service");
        assertEquals("newName", response.jsonPath().getString("service.name"));
        assertEquals("newAddress", response.jsonPath().getString("service.address"));
    }

    public void userGetsAllOfTheirServices(String userId, String token) {
        Response response = RestAssured.given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .get("http://localhost:8080/service/user/" + userId);

        assertEquals(200, response.getStatusCode(), "User should be able to get all of their services");
        Map<String, Object> jsonResponse = response.jsonPath().getMap("$");
        List<Object> services = (List<Object>) jsonResponse.get("services");
        assertEquals(2, services.size(), "Expected 2 services for user " + userId);
    }

    public void userGetsLastServiceStatus(int serviceId) {
        Response response = RestAssured.given()
                .contentType("application/json")
                .get("http://localhost:8080/service/" + serviceId + "/status/last");

        assertEquals(200, response.getStatusCode(), "User should be able to get last service status");

    }
}
