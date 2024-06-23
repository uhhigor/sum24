package org.example.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.api.exception.ServiceEntityNotFoundException;
import org.example.api.model.ExtendedServiceEntity;
import org.example.api.model.ServiceEntity;
import org.example.api.service.ServiceEntityService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OpenTsdbService {

    private final String putUrl = "http://opentsdb:4242/api/put";
    private final String queryUrl = "http://opentsdb:4242/api/query";

    private final RestTemplate restTemplate;
    private final ServiceEntityService serviceEntityService;

    @Autowired
    public OpenTsdbService(RestTemplate restTemplate, ServiceEntityService serviceEntityService) {
        this.restTemplate = restTemplate;
        this.serviceEntityService = serviceEntityService;
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    public void sendStatus(String metric, long timestamp, boolean status, Map<String, String> tags) {
        Map<String, Object> data = new HashMap<>();
        data.put("metric", metric);
        data.put("timestamp", timestamp);
        data.put("value", status ? 1 : 0);
        data.put("tags", tags);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            System.out.println("Error while creating request body: " + e.getMessage());
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentLength(requestBody.length());
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        restTemplate.postForObject(putUrl, request, String.class);
    }

    public void sendDetailedStatus(String metric, long timestamp, Map<String, Object> detailedStatus, Map<String, String> tags) {
        detailedStatus.forEach((key, value) -> {
            if (value instanceof Map) {
                Map<String, Object> metricsMap = (Map<String, Object>) value;
                metricsMap.forEach((metricKey, metricValue) -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("metric", metric);
                    data.put("timestamp", timestamp);
                    data.put("value", metricValue);
                    tags.put("metricName", metricKey);
                    data.put("tags", tags);

                    ObjectMapper objectMapper = new ObjectMapper();
                    String requestBody;
                    try {
                        requestBody = objectMapper.writeValueAsString(data);
                    } catch (JsonProcessingException e) {
                        System.out.println("Error while creating request body: " + e.getMessage());
                        return;
                    }

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setContentLength(requestBody.length());
                    HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

                    restTemplate.postForObject(putUrl, request, String.class);
                });
            } else {
                Map<String, Object> data = new HashMap<>();
                data.put("metric", metric);
                data.put("timestamp", timestamp);
                if (value instanceof Number) {
                    data.put("value", value);
                } else {
                    data.put("value", 0);
                }
                tags.put("metricName", key);
                data.put("tags", tags);

                ObjectMapper objectMapper = new ObjectMapper();
                String requestBody;
                try {
                    requestBody = objectMapper.writeValueAsString(data);
                } catch (JsonProcessingException e) {
                    System.out.println("Error while creating request body: " + e.getMessage());
                    return;
                }

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setContentLength(requestBody.length());
                HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

                restTemplate.postForObject(putUrl, request, String.class);
            }
        });
    }

    public String getAllMetricsOfService(Integer serviceId) {
        try {
            String query = "{serviceId=" + serviceId + ",metricName=";
            ServiceEntity s = this.serviceEntityService.getById(serviceId);
            if (s instanceof ExtendedServiceEntity) {
                ExtendedServiceEntity extendedServiceEntity = (ExtendedServiceEntity) s;
                List<String> fields = extendedServiceEntity.getFields();
                if (!fields.isEmpty()) {
                    for (String field : fields) {
                        query += field + "|";
                    }
                }
                StringBuilder stringBuilder = new StringBuilder(query);
                stringBuilder.setCharAt(query.length() - 1, '}');
                query = stringBuilder.toString();
                String fullUrl = queryUrl + "?start=0&m=sum:service{query}";
                ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class, query);
                return response.getBody();
            }

        } catch (ServiceEntityNotFoundException e) {
            System.out.println("Service not found!");
        } catch (HttpClientErrorException e) {
            System.out.println("No data for given field!");
        }

        return "";
    }

    public boolean getLastStatusOfService(Integer serviceId) {
        String query = "{serviceId=" + serviceId + "}";
        String fullUrl = queryUrl + "?start=0&m=sum:service.status{query}";
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class, query);
        String jsonString = response.getBody();
        JSONArray responseArray = new JSONArray(jsonString);

        JSONObject responseObject = responseArray.getJSONObject(0);
        JSONObject dpsObject = responseObject.getJSONObject("dps");

        List<String> keys = new ArrayList<>(dpsObject.keySet());
        keys.sort(Comparator.reverseOrder());

        String newestTimestamp = keys.get(0);
        int value = dpsObject.getInt(newestTimestamp);

        return value == 1;
    }

}
