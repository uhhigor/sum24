package org.example.api.util;

import org.example.api.exception.ServiceObjectStatusException;
import org.example.api.model.ServiceObject;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;

public class ServiceObjectStatus {
    public static boolean isOnline(ServiceObject serviceObject) {
        WebClient client = WebClient.create();
        try {
            client.get()
                    .uri(new URI(serviceObject.getAddress()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static ServiceObjectStatusResponse getDetailedStatus(ServiceObject serviceObject) throws ServiceObjectStatusException {
        try {
            return WebClient.create().get()
                    .uri(new URI(serviceObject.getAddress()))
                    .retrieve()
                    .bodyToMono(ServiceObjectStatusResponse.class)
                    .block();
        } catch (URISyntaxException e) {
            throw new ServiceObjectStatusException("Cannot connect to service");
        }
    }

    public record ServiceObjectStatusResponse(String status, Usage usage, String time, Long timeunix) {}
    public record Usage(double cpu, double memory, double storage) {}
}
