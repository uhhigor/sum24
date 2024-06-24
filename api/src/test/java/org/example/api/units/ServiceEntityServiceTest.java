package org.example.api.units;

import org.example.api.exception.ServiceEntityNotFoundException;
import org.example.api.exception.ServiceEntityStatusException;
import org.example.api.exception.UserNotFoundException;
import org.example.api.model.BasicServiceEntity;
import org.example.api.model.ExtendedServiceEntity;
import org.example.api.model.ServiceEntity;
import org.example.api.repository.BasicServiceEntityRepository;
import org.example.api.repository.ExtendedServiceEntityRepository;
import org.example.api.service.ServiceEntityService;
import org.example.api.users.data.User;
import org.example.api.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ServiceEntityServiceTest {

    @Mock
    private BasicServiceEntityRepository basicServiceEntityRepository;

    @Mock
    private ExtendedServiceEntityRepository extendedServiceEntityRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private ServiceEntityService serviceEntityService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllByUser_UserNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> serviceEntityService.getAllByUser(1));
    }

    @Test
    public void testSave_BasicServiceEntity() {
        BasicServiceEntity entity = new BasicServiceEntity();
        when(basicServiceEntityRepository.save(any(BasicServiceEntity.class))).thenReturn(entity);

        ServiceEntity result = serviceEntityService.save(entity);

        assertEquals(entity, result);
        verify(basicServiceEntityRepository, times(1)).save(entity);
        verify(extendedServiceEntityRepository, never()).save(any(ExtendedServiceEntity.class));
    }

    @Test
    public void testSave_ExtendedServiceEntity() {
        ExtendedServiceEntity entity = new ExtendedServiceEntity();
        when(extendedServiceEntityRepository.save(any(ExtendedServiceEntity.class))).thenReturn(entity);

        ServiceEntity result = serviceEntityService.save(entity);

        assertEquals(entity, result);
        verify(extendedServiceEntityRepository, times(1)).save(entity);
        verify(basicServiceEntityRepository, never()).save(any(BasicServiceEntity.class));
    }

    @Test
    public void testDelete_ServiceEntityNotFound() {
        when(basicServiceEntityRepository.existsById(anyInt())).thenReturn(false);
        when(extendedServiceEntityRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(ServiceEntityNotFoundException.class, () -> serviceEntityService.delete(1));
    }

    @Test
    public void testDelete_Success() throws ServiceEntityNotFoundException {
        when(basicServiceEntityRepository.existsById(anyInt())).thenReturn(true);

        serviceEntityService.delete(1);

        verify(basicServiceEntityRepository, times(1)).deleteById(1);
        verify(extendedServiceEntityRepository, times(1)).deleteById(1);
    }

    @Test
    public void testGetById_ServiceEntityNotFound() {
        when(basicServiceEntityRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(extendedServiceEntityRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ServiceEntityNotFoundException.class, () -> serviceEntityService.getById(1));
    }

    @Test
    public void testIsOnlineStatus_True() throws Exception {
        ServiceEntity serviceEntity = new BasicServiceEntity();
        serviceEntity.setAddress("http://example.com");

        WebClient.RequestHeadersUriSpec request = mock(WebClient.RequestHeadersUriSpec.class);

        when(webClient.get()).thenReturn(request);
        when(request.uri(any(URI.class))).thenReturn(request);
        when(request.retrieve()).thenThrow(WebClientResponseException.class);

        assertTrue(serviceEntityService.isOnlineStatus(serviceEntity));
    }

}
