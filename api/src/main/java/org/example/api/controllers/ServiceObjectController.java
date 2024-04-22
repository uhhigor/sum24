package org.example.api.controllers;

import org.example.api.exceptions.ServiceObjectServiceException;
import org.example.api.exceptions.ServiceObjectStatusException;
import org.example.api.logic.ServiceObjectStatus;
import org.example.api.services.ServiceObjectService;
import org.example.api.services.data.ServiceObjectStatusResponse;
import org.example.api.services.repositories.ServiceObjectRepository;
import org.example.api.services.data.ServiceObject;
import org.example.api.users.data.User;
import org.example.api.users.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Controller
@RequestMapping("/services")
public class ServiceObjectController {
    private final ServiceObjectService serviceObjectService;

    private final UserRepository userRepository;

    public ServiceObjectController(ServiceObjectService serviceObjectService, UserRepository userRepository) {
        this.serviceObjectService = serviceObjectService;
        this.userRepository = userRepository;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ServiceObject>> getAllUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(serviceObjectService.getAllUser(userId));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Object> add(@PathVariable Integer userId, @RequestBody ServiceObject.ServiceObjectDTO serviceObjectDTO) {
        try {
            return ResponseEntity.ok(serviceObjectService.add(userId, serviceObjectDTO));
        } catch (ServiceObjectServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody ServiceObject.ServiceObjectDTO serviceObjectDTO, @PathVariable Integer id) {
        try {
            return ResponseEntity.ok(serviceObjectService.update(id, serviceObjectDTO));
        } catch (ServiceObjectServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<ServiceObject>> getAll() {
        return ResponseEntity.ok(serviceObjectService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        serviceObjectService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(serviceObjectService.getById(id));
        } catch (ServiceObjectServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/status/online")
    public ResponseEntity<Boolean> getServiceStatus(@PathVariable Integer id) {
        try {
            ServiceObject serviceObject = serviceObjectService.getById(id);
            return ResponseEntity.ok(ServiceObjectStatus.isOnline(serviceObject));
        } catch (ServiceObjectServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/status/detailed")
    public ResponseEntity<ServiceObjectStatusResponse> getDetailedServiceStatus(@PathVariable Integer id) {
        try {
            ServiceObject serviceObject = serviceObjectService.getById(id);
            return ResponseEntity.ok(ServiceObjectStatus.getDetailedStatus(serviceObject));
        } catch (ServiceObjectServiceException | ServiceObjectStatusException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
