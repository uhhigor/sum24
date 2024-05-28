package org.example.api;

import org.example.api.exception.ServiceObjectServiceException;
import org.example.api.exception.ServiceObjectStatusException;
import org.example.api.model.ServiceObject;
import org.example.api.repository.ServiceObjectRepository;
import org.example.api.service.ServiceObjectService;
import org.example.api.users.repositories.UserRepository;
import org.example.api.util.ServiceObjectStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.api.users.data.User;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class ServiceObjectServiceTests {
    ServiceObjectRepository serviceObjectRepository;
    UserRepository userRepository;

    ServiceObjectService serviceObjectService;
   /* @BeforeEach
    public void setUp() {
        serviceObjectRepository = Mockito.mock(ServiceObjectRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        serviceObjectService = new ServiceObjectService(serviceObjectRepository, userRepository);

        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));

        ServiceObject serviceObject1 = ServiceObject.builder().name("Service 1").address("google.com").port(80).owner(user1).build();
        when(serviceObjectRepository.save(serviceObject1)).thenReturn(serviceObject1);
        when(serviceObjectRepository.findById(1)).thenReturn(Optional.of(serviceObject1));

        ServiceObject serviceObject2 = ServiceObject.builder().name("Service 2").address("onet.pl").port(80).owner(user2).build();
        when(serviceObjectRepository.save(serviceObject2)).thenReturn(serviceObject2);
        when(serviceObjectRepository.findById(2)).thenReturn(Optional.of(serviceObject2));

        ServiceObject serviceObject3 = ServiceObject.builder().name("Service 3").address("localhost").port(8082).owner(user1).build();
        when(serviceObjectRepository.save(serviceObject3)).thenReturn(serviceObject3);
        when(serviceObjectRepository.findById(3)).thenReturn(Optional.of(serviceObject3));
        List<ServiceObject> list = new ArrayList<>();
        list.add(serviceObject1);
        list.add(serviceObject2);
        list.add(serviceObject3);

        when(serviceObjectRepository.findAll()).thenReturn(list);
    }

    @Test
    public void testGetAll() {
        List<ServiceObject> list = serviceObjectService.getAll();
        assert list.size() == 3;
        assert list.getFirst().getName().equals("Service 1");
        assert list.get(1).getName().equals("Service 2");
        assert list.getLast().getName().equals("Service 3");

        assert list.getFirst().getOwner().getId() == 1;
        assert list.get(1).getOwner().getId() == 2;
        assert list.getLast().getOwner().getId() == 1;

        assert list.getFirst().getAddress().equals("google.com");
        assert list.get(1).getAddress().equals("onet.pl");
        assert list.getLast().getAddress().equals("localhost");

        assert list.getFirst().getPort() == 80;
        assert list.get(1).getPort() == 80;
        assert list.getLast().getPort() == 8082;
    }

    @Test
    public void testGetAllUser() {
        List<ServiceObject> list = serviceObjectService.getAll(1);
        assert list.size() == 2;
        assert list.getFirst().getName().equals("Service 1");
        assert list.getLast().getName().equals("Service 3");

        assert list.getFirst().getOwner().getId() == 1;
        assert list.getLast().getOwner().getId() == 1;

        assert list.getFirst().getAddress().equals("google.com");
        assert list.getLast().getAddress().equals("localhost");

        assert list.getFirst().getPort() == 80;
        assert list.getLast().getPort() == 8082;

        list = serviceObjectService.getAll(2);
        assert list.size() == 1;
        assert list.getFirst().getName().equals("Service 2");
        assert list.getFirst().getOwner().getId() == 2;
        assert list.getFirst().getAddress().equals("onet.pl");
        assert list.getFirst().getPort() == 80;
    }

    @Test
    public void testAdd() throws ServiceObjectServiceException {
        ServiceObject.ServiceObjectDTO serviceObjectDTO = new ServiceObject.ServiceObjectDTO("Service 4", "example.com", 8080);
        when(serviceObjectRepository.save(Mockito.any(ServiceObject.class))).thenReturn(ServiceObject.builder().name("Service 4").address("example.com").port(8080).owner(new User()).build());
        ServiceObject serviceObject = serviceObjectService.add(1, serviceObjectDTO);
        assert serviceObject.getName().equals("Service 4");
        assert serviceObject.getAddress().equals("example.com");
        assert serviceObject.getPort() == 8080;
    }

    @Test
    public void testUpdate() throws ServiceObjectServiceException {
        ServiceObject.ServiceObjectDTO serviceObjectDTO = new ServiceObject.ServiceObjectDTO("Service 5", "example2.com", 8081);
        when(serviceObjectRepository.save(Mockito.any(ServiceObject.class))).thenReturn(ServiceObject.builder().name("Service 5").address("example2.com").port(8081).owner(new User()).build());
        ServiceObject serviceObject = serviceObjectService.update(1, serviceObjectDTO);

        assert serviceObject.getName().equals("Service 5");
        assert serviceObject.getAddress().equals("example2.com");
        assert serviceObject.getPort() == 8081;
    }

    @Test
    public void testOnlineStatus() throws ServiceObjectServiceException {
        assert ServiceObjectStatus.isOnline(serviceObjectService.getById(1));
        assert ServiceObjectStatus.isOnline(serviceObjectService.getById(2));
        assert !ServiceObjectStatus.isOnline(serviceObjectService.getById(3));
    }*/
}
