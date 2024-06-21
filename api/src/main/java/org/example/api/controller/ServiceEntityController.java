package org.example.api.controller;

import org.example.api.dto.ServiceEntityDto;
import org.example.api.exception.ServiceEntityException;
import org.example.api.exception.ServiceEntityNotFoundException;
import org.example.api.exception.UserNotFoundException;
import org.example.api.model.BasicServiceEntity;
import org.example.api.model.ExtendedServiceEntity;
import org.example.api.model.ServiceEntity;
import org.example.api.model.ServiceEntityBuilder;
import org.example.api.service.ServiceEntityService;
import org.example.api.users.data.User;
import org.example.api.users.repositories.UserRepository;
import org.example.api.util.OpenTsdbService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/service")
public class ServiceEntityController {
    private final ServiceEntityService serviceEntityService;

    private final UserRepository userRepository;

    public ServiceEntityController(ServiceEntityService serviceEntityService, UserRepository userRepository) {
        this.serviceEntityService = serviceEntityService;
        this.userRepository = userRepository;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ServiceEntityDto.ListResponse> getAllUser(@PathVariable Integer userId) {
        try {
            List<ServiceEntity> result = serviceEntityService.getAllByUser(userId);
            return ResponseEntity.ok(new ServiceEntityDto.ListResponse(result));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<ServiceEntityDto.Response> add(@PathVariable Integer userId, @RequestBody ServiceEntityDto.Request request) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        ServiceEntity service;
        if (request.fields == null || request.fields.isEmpty()) {
            service = new ServiceEntityBuilder(BasicServiceEntity.class)
                    .setName(request.name)
                    .setAddress(request.address)
                    .setOwner(user)
                    .build();
        } else {
            service = new ServiceEntityBuilder(ExtendedServiceEntity.class)
                    .setName(request.name)
                    .setAddress(request.address)
                    .setOwner(user)
                    .setFields(request.fields)
                    .build();
        }
        service = serviceEntityService.save(service);
        return ResponseEntity.ok(new ServiceEntityDto.Response("Service added successfully", service));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ServiceEntityDto.Response> update(@RequestBody ServiceEntityDto.Request request, @PathVariable Integer id) {
        ServiceEntity service;
        try {
            service = serviceEntityService.getById(id);
        } catch (ServiceEntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        if(request.name != null) {
            service.setName(request.name);
        }
        if(request.address != null) {
            service.setAddress(request.address);
        }
        if(request.fields != null) {
            if(service instanceof ExtendedServiceEntity) {
                ((ExtendedServiceEntity) service).setFields(request.fields);
            } else {
                return ResponseEntity.badRequest().body(new ServiceEntityDto.Response("Fields can only be set for ExtendedServiceEntity"));
            }
        }

        service = serviceEntityService.save(service);
        return ResponseEntity.ok(new ServiceEntityDto.Response("Service " + id + " updated successfully", service));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceEntityDto.Response> delete(@PathVariable Integer id) {
        try {
            serviceEntityService.delete(id);
        } catch (ServiceEntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ServiceEntityDto.Response("Service " + id + " deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceEntityDto.Response> get(@PathVariable Integer id) {
        try {
            ServiceEntity service = serviceEntityService.getById(id);
            return ResponseEntity.ok(new ServiceEntityDto.Response(service));
        } catch (ServiceEntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/status/last")
    public ResponseEntity<Object> getLastStatus(@PathVariable Integer id) {
        try {
            OpenTsdbService openTsdbService = new OpenTsdbService(new RestTemplate());
            return ResponseEntity.ok(openTsdbService.getLatestMetricValue(id.toString()));

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
