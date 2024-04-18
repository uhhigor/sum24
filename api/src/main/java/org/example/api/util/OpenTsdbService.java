package org.example.api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OpenTsdbService {

    private final String putUrl = "http://opentsdb:4242/api/put";
    private final String queryUrl = "http://opentsdb:4242/api/query";

    private final RestTemplate restTemplate;

    public OpenTsdbService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    public void sendPingResult(String serviceId, String serviceStatus) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"metric\":\"service.status." + serviceId.toLowerCase() + "\"," +
                "\"timestamp\":" + System.currentTimeMillis() / 1000 + "," +
                "\"value\":" + (serviceStatus.equals("true") ? 1 : 0) + "," +
                "\"tags\":{\"status\":\"" + (serviceStatus.equals("true") ? "up" : "down") + "\", \"type\":\"status\"}}";

        headers.setContentLength(requestBody.getBytes().length);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(putUrl, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            System.out.println("Ping result sent successfully for service: " + serviceId);
        } else {
            System.err.println("Failed to send ping result for service: " + serviceId + ". Response: " + response.getStatusCode());
        }
    }

    public int getLatestMetricValue(String serviceId) {
        String query = "{\n" +
                "    \"start\": " + ((System.currentTimeMillis() / 1000) - 6000) + ",\n" +
                "    \"end\": " + System.currentTimeMillis() / 1000 + ",\n" +
                "    \"queries\": [\n" +
                "        {\n" +
                "            \"aggregator\": \"sum\",\n" +
                "            \"metric\": \"service.status." + serviceId.toLowerCase() + "\",\n" +
                "            \"tags\": {\n" +
                "                \"type\": \"status\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(query, headers);

        ResponseEntity<String> response = restTemplate.exchange(queryUrl, HttpMethod.POST, request, String.class);
        System.out.println(response.getBody());
        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode dpsNode = root.get(0).get("dps");
                Iterator<String> iterator = dpsNode.fieldNames();
                String lastTimestamp = "";
                while (iterator.hasNext()) {
                    lastTimestamp = iterator.next();
                }
                int latestValue = dpsNode.get(lastTimestamp).asInt();
                return latestValue;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            System.err.println("Failed to retrieve latest metric value. Response: " + response.getStatusCode());
            return 0;
        }
    }
}
