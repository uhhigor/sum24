package org.example.api.logic;

import org.example.api.exceptions.ServiceObjectStatusException;
import org.example.api.services.data.ServiceObject;
import org.example.api.services.data.ServiceObjectStatusResponse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

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
        return null;
    }
}
