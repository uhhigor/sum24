package org.example.api.services;

import org.example.api.exceptions.ServiceObjectException;
import org.example.api.exceptions.ServiceObjectServiceException;
import org.example.api.services.data.ServiceObject;
import org.example.api.services.repositories.ServiceObjectRepository;
import org.example.api.users.data.User;
import org.example.api.users.repositories.UserRepository;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceObjectService {
    private final ServiceObjectRepository serviceObjectRepository;

    private final UserRepository userRepository;

    public ServiceObjectService(ServiceObjectRepository serviceObjectRepository, UserRepository userRepository) {
        this.serviceObjectRepository = serviceObjectRepository;
        this.userRepository = userRepository;
    }

    public List<ServiceObject> getAll() {
        return StreamUtils.createStreamFromIterator(serviceObjectRepository.findAll().iterator()).toList();
    }

    public List<ServiceObject> getAll(Integer userId) {
        return StreamUtils.createStreamFromIterator(serviceObjectRepository.findAll().iterator())
                .filter(service -> service.getOwner().getId().equals(userId))
                .toList();
    }

    public ServiceObject add(int userId, ServiceObject.ServiceObjectDTO serviceObjectDTO) throws ServiceObjectServiceException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceObjectServiceException("User not found"));
        ServiceObject serviceObject = ServiceObject.builder()
                .name(serviceObjectDTO.getName())
                .address(serviceObjectDTO.getAddress())
                .port(serviceObjectDTO.getPort())
                .owner(user)
                .build();

        return serviceObjectRepository.save(serviceObject);
    }

    public ServiceObject update(ServiceObject serviceObject) throws ServiceObjectException {
        if(serviceObject.getId() == null)
            throw new ServiceObjectException("Service object id is required");
        return serviceObjectRepository.save(serviceObject);
    }

    public ServiceObject update(Integer id, ServiceObject.ServiceObjectDTO serviceObjectDTO) throws ServiceObjectServiceException {
        ServiceObject serviceObject = getById(id);
        serviceObject.setName(serviceObjectDTO.getName());
        serviceObject.setAddress(serviceObjectDTO.getAddress());
        serviceObject.setPort(serviceObjectDTO.getPort());
        return serviceObjectRepository.save(serviceObject);
    }

    public void delete(Integer id) {
        serviceObjectRepository.deleteById(id);
    }

    public ServiceObject getById(Integer id) throws ServiceObjectServiceException {
        return serviceObjectRepository.findById(id).orElseThrow(() -> new ServiceObjectServiceException("Service object not found"));
    }



}
