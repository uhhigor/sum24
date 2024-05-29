package org.example.api.util;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

public class SendRequest {

    private final RestTemplate restTemplate;

    public SendRequest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String sendRequest(String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    /*public ServiceObjectStatus.ServiceObjectStatusResponse sendDetailedRequest(String url) {
        WebClient.ResponseSpec responseSpec = WebClient.create().get().uri(url).retrieve();

        // Check the content type of the response
        MediaType contentType = responseSpec.toBodilessEntity().block().getHeaders().getContentType();
        if (contentType != null && contentType.includes(MediaType.APPLICATION_JSON)) {
            // If it's JSON, process it as ServiceObjectStatusResponse
            return responseSpec.bodyToMono(ServiceObjectStatus.ServiceObjectStatusResponse.class).block();
        } else {
            // If it's not JSON, handle it appropriately
            System.err.println("Unexpected content type: " + contentType);
            return null;
        }
    }*/


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
