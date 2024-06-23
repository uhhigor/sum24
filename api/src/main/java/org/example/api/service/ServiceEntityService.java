package org.example.api.service;

import org.example.api.exception.ServiceEntityNotFoundException;
import org.example.api.exception.ServiceEntityStatusException;
import org.example.api.exception.UserNotFoundException;
import org.example.api.model.BasicServiceEntity;
import org.example.api.model.ExtendedServiceEntity;
import org.example.api.model.ServiceEntity;
import org.example.api.repository.BasicServiceEntityRepository;
import org.example.api.repository.ExtendedServiceEntityRepository;
import org.example.api.users.data.User;
import org.example.api.users.repositories.UserRepository;
import org.hibernate.Hibernate;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.*;

@Service
public class ServiceEntityService {
    private final BasicServiceEntityRepository basicServiceEntityRepository;

    private final ExtendedServiceEntityRepository extendedServiceEntityRepository;

    private final UserRepository userRepository;

    public ServiceEntityService(BasicServiceEntityRepository basicServiceEntityRepository, ExtendedServiceEntityRepository extendedServiceEntityRepository, UserRepository userRepository) {
        this.basicServiceEntityRepository = basicServiceEntityRepository;
        this.extendedServiceEntityRepository = extendedServiceEntityRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public Set<ServiceEntity> getAllByUser(Integer userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        Set<ServiceEntity> result = new HashSet<>();

        Iterable<BasicServiceEntity> basicServiceEntities = basicServiceEntityRepository.findAll();
        for (ServiceEntity serviceEntity : basicServiceEntities) {
            if (serviceEntity.getOwner().getId().equals(user.getId())) {
                result.add(serviceEntity);
            }
        }

        Iterable<ExtendedServiceEntity> extendedServiceEntities = extendedServiceEntityRepository.findAll();
        for (ServiceEntity serviceEntity : extendedServiceEntities) {
            if (serviceEntity.getOwner().getId().equals(user.getId())) {
                result.add(serviceEntity);
            }
        }

        return result;
    }

    public synchronized ServiceEntity save(ServiceEntity serviceEntity) {
        if(serviceEntity instanceof ExtendedServiceEntity service) {
            return extendedServiceEntityRepository.save(service);
        } else if(serviceEntity instanceof BasicServiceEntity service) {
            return basicServiceEntityRepository.save(service);
        }
        return null;
    }

    public void delete(Integer id) throws ServiceEntityNotFoundException {
        if(!basicServiceEntityRepository.existsById(id) && !extendedServiceEntityRepository.existsById(id)) {
            throw new ServiceEntityNotFoundException("Service object not found");
        }
        basicServiceEntityRepository.deleteById(id);
        extendedServiceEntityRepository.deleteById(id);
    }

    public ServiceEntity getById(Integer id) throws ServiceEntityNotFoundException {
        ServiceEntity serviceEntity = basicServiceEntityRepository.findById(id).orElse(null);
        if (serviceEntity == null) {
            serviceEntity = extendedServiceEntityRepository.findById(id).orElse(null);
        }
        if (serviceEntity == null) {
            throw new ServiceEntityNotFoundException("Service object not found");
        }
        return serviceEntity;
    }

    public boolean isOnlineStatus(ServiceEntity serviceEntity) {
        WebClient client = WebClient.create();
        try {
            client.get()
                    .uri(new URI(serviceEntity.getAddress()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, Object> getDetailedStatus(ExtendedServiceEntity serviceEntity) throws ServiceEntityStatusException {
        Hibernate.initialize(serviceEntity.getFields());
        String json;
        try {
            json = WebClient.create().get()
                    .uri(new URI(serviceEntity.getAddress()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new ServiceEntityStatusException("Cannot connect to service");
        }
        JSONObject jsonObject = new JSONObject(json);
        Map<String, Object> jsonMap = jsonObject.toMap();

        for(String key : jsonMap.keySet()) {
            if (!serviceEntity.getFields().contains(key)) {
                serviceEntity.addField(key);
            }
        }
        return jsonMap;
    }
}
