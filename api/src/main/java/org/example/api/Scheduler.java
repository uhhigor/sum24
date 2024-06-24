package org.example.api;

import org.example.api.exception.ServiceEntityStatusException;
import org.example.api.exception.UserNotFoundException;
import org.example.api.model.ExtendedServiceEntity;
import org.example.api.model.ServiceEntity;
import org.example.api.service.ServiceEntityService;
import org.example.api.util.SendRequest;
import org.example.api.util.OpenTsdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Scheduler {
    SendRequest sendRequest;
    private OpenTsdbService openTsdbService;
    private final ServiceEntityService serviceEntityService;

    @Autowired
    public Scheduler(ServiceEntityService serviceEntityService) {
        this.sendRequest = new SendRequest(new RestTemplate());
        this.openTsdbService = new OpenTsdbService(new RestTemplate(), serviceEntityService);
        this.serviceEntityService = serviceEntityService;
    }

    private String email;
    private String token;
    private int userId;

    private Map<String, Boolean> emailSentMap = new HashMap<>();

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void callServiceData() {
        if (email == null) {
            return;
        }

        try {
            List<ServiceEntity> serviceEntityList = this.serviceEntityService.getAllByUser(this.userId).stream().toList();
            for (ServiceEntity serviceEntity : serviceEntityList) {
                Map<String, String> tags = new HashMap<>();
                tags.put("serviceId", String.valueOf(serviceEntity.getId()));
                tags.put("serviceName", serviceEntity.getName());

                boolean status = serviceEntityService.isOnlineStatus(serviceEntity);
                long timestamp = System.currentTimeMillis() / 1000L;
                openTsdbService.sendStatus("service.status", timestamp, status, tags);

                if (serviceEntity instanceof ExtendedServiceEntity) {
                    try {
                        Map<String, Object> detailedStatus = serviceEntityService.getDetailedStatus((ExtendedServiceEntity) serviceEntity);
                        Integer timeunix = (Integer) detailedStatus.get("timeunix");
                        openTsdbService.sendDetailedStatus("service", timeunix, detailedStatus, tags);
                    } catch (ServiceEntityStatusException e) {
                        System.out.println("Failed to get detailed status for service: " + serviceEntity.getId());
                    }
                }

                if (!status && !emailSentMap.getOrDefault(serviceEntity.getId(), false)) {
                    System.out.println("Service " + serviceEntity.getId() + " is offline");
                    if (email == null) {
                        email = "admin@localhost";
                    }
                    String emailResponse = sendRequest.sendEmailRequest(email, serviceEntity.getId().toString(), serviceEntity.getName(), token);

                    emailSentMap.put(serviceEntity.getId().toString(), true);
                } else if (!emailSentMap.getOrDefault(serviceEntity.getId(), false) || status) {
                    emailSentMap.put(serviceEntity.getId().toString(), false);
                }
            }
        } catch (UserNotFoundException e) {
            System.out.println("User Not Found");
        }
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
