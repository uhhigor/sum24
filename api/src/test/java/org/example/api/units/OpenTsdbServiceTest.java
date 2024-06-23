package org.example.api.units;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.api.exception.ServiceEntityNotFoundException;
import org.example.api.model.ExtendedServiceEntity;
import org.example.api.model.ServiceEntity;
import org.example.api.service.ServiceEntityService;
import org.example.api.util.OpenTsdbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OpenTsdbServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ServiceEntityService serviceEntityService;

    @InjectMocks
    private OpenTsdbService openTsdbService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSendStatus() throws JsonProcessingException {
        String metric = "test.metric";
        long timestamp = System.currentTimeMillis();
        boolean status = true;
        Map<String, String> tags = new HashMap<>();
        tags.put("tag1", "value1");

        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("metric", metric);
        expectedData.put("timestamp", timestamp);
        expectedData.put("value", 1);
        expectedData.put("tags", tags);

        String expectedRequestBody = objectMapper.writeValueAsString(expectedData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentLength(expectedRequestBody.length());
        HttpEntity<String> expectedRequest = new HttpEntity<>(expectedRequestBody, headers);

        openTsdbService.sendStatus(metric, timestamp, status, tags);

        ArgumentCaptor<HttpEntity> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).postForObject(eq("http://opentsdb:4242/api/put"), captor.capture(), eq(String.class));
        assertEquals(expectedRequest, captor.getValue());
    }

    @Test
    public void testSendDetailedStatus() throws JsonProcessingException {
        String metric = "test.metric";
        long timestamp = System.currentTimeMillis();
        Map<String, Object> detailedStatus = new HashMap<>();
        Map<String, Object> nestedMap = new HashMap<>();
        nestedMap.put("nestedMetric", 123);
        detailedStatus.put("outerMetric", nestedMap);
        Map<String, String> tags = new HashMap<>();
        tags.put("tag1", "value1");

        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("metric", metric);
        expectedData.put("timestamp", timestamp);
        expectedData.put("value", 123);
        tags.put("metricName", "nestedMetric");
        expectedData.put("tags", tags);

        String expectedRequestBody = objectMapper.writeValueAsString(expectedData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentLength(expectedRequestBody.length());
        HttpEntity<String> expectedRequest = new HttpEntity<>(expectedRequestBody, headers);

        openTsdbService.sendDetailedStatus(metric, timestamp, detailedStatus, tags);

        ArgumentCaptor<HttpEntity> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).postForObject(eq("http://opentsdb:4242/api/put"), captor.capture(), eq(String.class));
        assertEquals(expectedRequest, captor.getValue());
    }

    @Test
    public void testGetAllMetricsOfService_Success() throws ServiceEntityNotFoundException {
        Integer serviceId = 1;
        ExtendedServiceEntity serviceEntity = mock(ExtendedServiceEntity.class);
        List<String> fields = Arrays.asList("metric1", "metric2");
        when(serviceEntity.getFields()).thenReturn(fields);
        when(serviceEntityService.getById(serviceId)).thenReturn(serviceEntity);

        String query = "{serviceId=1,metricName=metric1|metric2}";
        String fullUrl = "http://opentsdb:4242/api/query?start=0&m=sum:service{query}";
        String responseBody = "[{\"metric\":\"service.status\",\"tags\":{\"serviceId\":\"1\"},\"dps\":{\"1625583600\":1}}]";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        when(restTemplate.getForEntity(fullUrl, String.class, query)).thenReturn(responseEntity);

        String result = openTsdbService.getAllMetricsOfService(serviceId);

        assertEquals(responseBody, result);
    }

    @Test
    public void testGetAllMetricsOfService_ServiceNotFound() throws ServiceEntityNotFoundException {
        Integer serviceId = 1;
        when(serviceEntityService.getById(serviceId)).thenThrow(new ServiceEntityNotFoundException("Service not found"));

        String result = openTsdbService.getAllMetricsOfService(serviceId);

        assertEquals("", result);
    }
}

