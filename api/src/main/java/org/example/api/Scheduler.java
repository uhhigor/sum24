package org.example.api;

import org.example.api.util.OpenTsdbService;
import org.example.api.util.SendRequest;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class Scheduler {
    SendRequest sendRequest = new SendRequest(new RestTemplate());

    private String email;
    private String token;
    private int userId;

    private Map<String, Boolean> emailSentMap = new HashMap<>();
    OpenTsdbService openTsdbService = new OpenTsdbService(new RestTemplate());

    @Scheduled(fixedRate = 30000)
    public void callServiceData() {
        if(email == null) {
            return;
        }
        Map<String, String> services = fetchServicesData();
        System.out.println("Services: " + services);
        for (Map.Entry<String, String> service : services.entrySet()) {
            String response = sendRequest.sendRequest("http://localhost:8080/service/" + service.getKey(), this.token);

            System.out.println("Service " + service.getKey() + " is " + response);

//            openTsdbService.sendPingResult(service.getKey(), response);
//            if (response.equals("false") && !emailSentMap.getOrDefault(service.getKey(), false)) {
//                System.out.println("Service " + service.getKey() + " is offline");
//                if(email == null) {
//                    email = "admin@localhost";
//                }
//                String emailResponse = sendRequest.sendEmailRequest(email, service.getKey(), service.getValue().substring(1, service.getValue().length() - 1));
//
//                emailSentMap.put(service.getKey(), true);
//            } else if (!emailSentMap.getOrDefault(service.getKey(), false) || response.equals("true")) {
//                emailSentMap.put(service.getKey(), false);
//            }
        }
    }

//    @Scheduled(fixedRate = 30000)
//    public void sendServiceData() {
//
//        Map<String, String> services = fetchServicesData();
//
//        for (Map.Entry<String, String> service : services.entrySet()) {
//            try {
//                RestTemplate restTemplate = new RestTemplate();
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_JSON);
//                headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//                HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
//
////                ResponseEntity<Service.ServiceObjectStatusResponse> responseEntity = restTemplate.exchange(
////                        "http://localhost:8080/services/" + service.getKey(),
////                        HttpMethod.GET,
////                        entity,
////                        ServiceObjectStatus.ServiceObjectStatusResponse.class);
////
////                ServiceObjectStatus.ServiceObjectStatusResponse response = responseEntity.getBody();
//                //openTsdbService.sendUsageMetrics(service.getKey(), response.usage());
//            } catch (Exception e) {
//                System.out.println("An error occurred while processing service " + service.getKey() + ": " + e.getMessage());
//            }
//        }
//    }

    private Map<String, String> fetchServicesData() {
        String response = sendRequest.sendRequest("http://localhost:8080/service/user/" + this.userId, this.token);
        System.out.println("Response: " + response);
        String[] jsonObjects = response.substring(12, response.length() - 2).split("},\\{");
        if (jsonObjects.length == 0) {
            return new HashMap<>();
        }

        Map<String, String> idMap = new HashMap<>();

        for (String jsonObject : jsonObjects) {
            String[] pairs = jsonObject.split(",");
            String[] id = pairs[0].split(":");
            String[] name = pairs[1].split(":");
            idMap.put(id[1].trim(), name[1].trim());
        }

        return idMap;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }



}
