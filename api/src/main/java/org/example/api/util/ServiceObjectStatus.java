package org.example.api.util;

import org.example.api.exception.ServiceObjectStatusException;
import org.example.api.model.ServiceObject;
import org.example.api.serviceObject.data.ServiceObjectStatusResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

public class ServiceObjectStatus {
    public static boolean isOnline(ServiceObject serviceObject) {
        try (Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(serviceObject.getAddress(), serviceObject.getPort()));
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static ServiceObjectStatusResponse getDetailedStatus(ServiceObject serviceObject) throws ServiceObjectStatusException {
        try {
            return WebClient.create().get()
                    .uri(new URI("http://" + serviceObject.getAddress() + ":" + serviceObject.getPort() + "/status"))
                    .retrieve()
                    .bodyToMono(ServiceObjectStatusResponse.class)
                    .block();
        } catch (URISyntaxException e) {
            throw new ServiceObjectStatusException("Cannot connect to service");
        }
    }

    public record ServiceObjectStatusResponse(float cpu_usage, float memory_usage, String message) {}
}
