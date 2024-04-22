package org.example.api.controller;

import org.example.api.exception.ServiceObjectServiceException;
import org.example.api.exception.ServiceObjectStatusException;
import org.example.api.util.ServiceObjectStatus;
import org.example.api.service.ServiceObjectService;
import org.example.api.model.ServiceObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/services")
public class ServiceObjectController {
    private final ServiceObjectService serviceObjectService;

    public ServiceObjectController(ServiceObjectService serviceObjectService) {
        this.serviceObjectService = serviceObjectService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ServiceObject>> getAllUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(serviceObjectService.getAll(userId));
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
    public ResponseEntity<Object> getDetailedServiceStatus(@PathVariable Integer id) {
        try {
            ServiceObject serviceObject = serviceObjectService.getById(id);
            return ResponseEntity.ok(ServiceObjectStatus.getDetailedStatus(serviceObject));
        } catch (ServiceObjectServiceException e) {
            return ResponseEntity.notFound().build();
        } catch (ServiceObjectStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
