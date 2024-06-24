package org.example.api.component;

import org.example.api.exception.ServiceEntityNotFoundException;
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
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ServiceComponentTest {

    @MockBean
    private BasicServiceEntityRepository basicServiceEntityRepository;

    @MockBean
    private ExtendedServiceEntityRepository extendedServiceEntityRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ServiceEntityService serviceEntityService;

    @MockBean
    private ServiceEntity serviceEntity;

    private WebClient webClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        webClient = WebClient.builder().build();
        when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    public void testGetAllByUser_Success() throws UserNotFoundException {
        Integer userId = 1;
        User user = new User();
        user.setId(userId);

        BasicServiceEntity basicServiceEntity = new BasicServiceEntity();
        basicServiceEntity.setOwner(user);

        ExtendedServiceEntity extendedServiceEntity = new ExtendedServiceEntity();
        extendedServiceEntity.setOwner(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(basicServiceEntityRepository.findAll()).thenReturn(Set.of(basicServiceEntity));
        when(extendedServiceEntityRepository.findAll()).thenReturn(Set.of(extendedServiceEntity));

        Set<ServiceEntity> result = serviceEntityService.getAllByUser(userId);

        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllByUser_UserNotFound() {
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> serviceEntityService.getAllByUser(userId));
    }

    @Test
    public void testSave_BasicServiceEntity() {
        BasicServiceEntity basicServiceEntity = new BasicServiceEntity();

        when(basicServiceEntityRepository.save(basicServiceEntity)).thenReturn(basicServiceEntity);

        ServiceEntity result = serviceEntityService.save(basicServiceEntity);

        assertEquals(basicServiceEntity, result);
    }

    @Test
    public void testSave_ExtendedServiceEntity() {
        ExtendedServiceEntity extendedServiceEntity = new ExtendedServiceEntity();

        when(extendedServiceEntityRepository.save(extendedServiceEntity)).thenReturn(extendedServiceEntity);

        ServiceEntity result = serviceEntityService.save(extendedServiceEntity);

        assertEquals(extendedServiceEntity, result);
    }

    @Test
    public void testDelete_ServiceEntityNotFound() {
        Integer id = 1;

        when(basicServiceEntityRepository.existsById(id)).thenReturn(false);
        when(extendedServiceEntityRepository.existsById(id)).thenReturn(false);

        assertThrows(ServiceEntityNotFoundException.class, () -> serviceEntityService.delete(id));
    }

    @Test
    public void testDelete_Success() throws ServiceEntityNotFoundException {
        Integer id = 1;

        when(basicServiceEntityRepository.existsById(id)).thenReturn(true);

        serviceEntityService.delete(id);

        verify(basicServiceEntityRepository, times(1)).deleteById(id);
    }

    @Test
    public void testGetById_Success() throws ServiceEntityNotFoundException {
        Integer id = 1;
        BasicServiceEntity basicServiceEntity = new BasicServiceEntity();
        basicServiceEntity.setId(id);

        when(basicServiceEntityRepository.findById(id)).thenReturn(Optional.of(basicServiceEntity));

        ServiceEntity result = serviceEntityService.getById(id);

        assertEquals(basicServiceEntity, result);
    }

    @Test
    public void testGetById_ServiceEntityNotFound() {
        Integer id = 1;

        when(basicServiceEntityRepository.findById(id)).thenReturn(Optional.empty());
        when(extendedServiceEntityRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ServiceEntityNotFoundException.class, () -> serviceEntityService.getById(id));
    }

}
