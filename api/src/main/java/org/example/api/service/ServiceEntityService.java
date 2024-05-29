package org.example.api.service;

import org.example.api.exception.ServiceEntityNotFoundException;
import org.example.api.exception.UserNotFoundException;
import org.example.api.model.ServiceEntity;
import org.example.api.repository.ServiceEntityRepository;
import org.example.api.users.data.User;
import org.example.api.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceEntityService {
    private final ServiceEntityRepository serviceEntityRepository;

    private final UserRepository userRepository;

    public ServiceEntityService(ServiceEntityRepository serviceEntityRepository, UserRepository userRepository) {
        this.serviceEntityRepository = serviceEntityRepository;
        this.userRepository = userRepository;
    }
    public List<ServiceEntity> getAllByUser(Integer userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Iterable<ServiceEntity> serviceEntities = serviceEntityRepository.findAll();
        List<ServiceEntity> result = new ArrayList<>();
        for (ServiceEntity serviceEntity : serviceEntities) {
            if (serviceEntity.getOwner().equals(user)) {
                result.add(serviceEntity);
            }
        }
        return result;
    }

    public ServiceEntity save(ServiceEntity serviceEntity) {
        return serviceEntityRepository.save(serviceEntity);
    }

    public void delete(Integer id) throws ServiceEntityNotFoundException {
        if(!serviceEntityRepository.existsById(id)) {
            throw new ServiceEntityNotFoundException("Service object not found");
        }
        serviceEntityRepository.deleteById(id);
    }

    public ServiceEntity getById(Integer id) throws ServiceEntityNotFoundException {
        return serviceEntityRepository.findById(id).orElseThrow(() -> new ServiceEntityNotFoundException("Service object not found"));
    }



}
