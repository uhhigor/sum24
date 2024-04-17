package org.example.api.logic;

import org.example.api.exceptions.ServiceStatusException;
import org.example.api.services.data.Service;
import org.example.api.services.data.ServiceStatusResponse;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServiceStatus {
    public static boolean isOnline(Service service) {
        try (Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(service.getAddress(), service.getPort()));
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static ServiceStatusResponse getDetailedStatus(Service service) throws ServiceStatusException {
        return null;
    }
}
