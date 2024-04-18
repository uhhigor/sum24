package org.example.api;

import org.example.api.util.OpenTsdbService;
import org.example.api.util.SendRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class Scheduler {
    SendRequest sendRequest = new SendRequest(new RestTemplate());

    private Map<String, Boolean> emailSentMap = new HashMap<>();
    OpenTsdbService openTsdbService = new OpenTsdbService(new RestTemplate());

    @Scheduled(fixedRate = 30000)
    public void callServiceStatus() {

        Map<String, String> services = fetchServicesData();

        for (Map.Entry<String, String> service : services.entrySet()) {
            String response = sendRequest.sendRequest("http://localhost:8080/services/" + service.getKey() + "/status/online");

            openTsdbService.sendPingResult(service.getKey(), response);

            if (response.equals("false") && !emailSentMap.getOrDefault(service.getKey(), false)) {
                System.out.println("Service " + service.getKey() + " is offline");
                String emailResponse = sendRequest.sendEmailRequest("stasiak.agnieszka567@gmail.com", service.getKey(), service.getValue());

                emailSentMap.put(service.getKey(), true);
            } else if (!emailSentMap.getOrDefault(service.getKey(), false) || response.equals("true")) {
                emailSentMap.put(service.getKey(), false);
            }
        }
    }

    private Map<String, String> fetchServicesData() {
        String response = sendRequest.sendRequest("http://localhost:8080/services/");
        String[] jsonObjects = response.substring(2, response.length() - 2).split("},\\{");

        Map<String, String> idMap = new HashMap<>();

        for (String jsonObject : jsonObjects) {
            String[] pairs = jsonObject.split(",");
            String[] id = pairs[0].split(":");
            String[] name = pairs[1].split(":");
            idMap.put(id[1].trim(), name[1].trim());
        }

        return idMap;
    }

}
