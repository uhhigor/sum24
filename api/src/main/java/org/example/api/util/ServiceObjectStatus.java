package org.example.api.util;

import org.example.api.exception.ServiceEntityStatusException;
import org.example.api.model.BasicServiceEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;

public class ServiceObjectStatus {
    public static boolean isOnline(BasicServiceEntity basicServiceEntity) {
        WebClient client = WebClient.create();
        try {
            client.get()
                    .uri(new URI(basicServiceEntity.getAddress()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static ServiceObjectStatusResponse getDetailedStatus(BasicServiceEntity basicServiceEntity) throws ServiceEntityStatusException {
        try {
            return WebClient.create().get()
                    .uri(new URI(basicServiceEntity.getAddress()))
                    .retrieve()
                    .bodyToMono(ServiceObjectStatusResponse.class)
                    .block();
        } catch (URISyntaxException e) {
            throw new ServiceEntityStatusException("Cannot connect to service");
        }
    }

    public record ServiceObjectStatusResponse(String status, Usage usage, String time, Long timeunix) {}
    public record Usage(double cpu, double memory, double storage) {}
}
