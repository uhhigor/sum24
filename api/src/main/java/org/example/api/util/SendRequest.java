package org.example.api.util;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class SendRequest {

    private final RestTemplate restTemplate;

    public SendRequest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String sendRequest(String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    private String sendPostRequestWithBody(String url, String requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        return response.getBody();
    }

    public String sendEmailRequest (String to, String serviceId, String serviceName) {
        String url = "http://localhost:8080/email/send";
        String requestBody =   "{"
                + "  \"to\": \"" + to + "\","
                + "  \"subject\": \"Service " + serviceId + " is down\","
                + "  \"body\": \"Service "+ serviceName + " with id: " + serviceId + " is down.\""
                + "}";
        return sendPostRequestWithBody(url, requestBody);
    }
}
