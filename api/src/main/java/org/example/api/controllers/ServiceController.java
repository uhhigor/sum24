package org.example.api.controllers;

import org.example.api.services.repositories.ServiceRepository;
import org.example.api.services.data.Service;
import org.example.api.services.data.ServiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/services")
public class ServiceController {
    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/")
    public @ResponseBody Iterable<Service> getAll() {
        return serviceRepository.findAll();
    }

    @PostMapping("/")
    public ResponseEntity<Integer> add(@RequestBody ServiceDTO serviceDTO) {
        try {
            Service service = serviceRepository.save(serviceDTO.toService());
            return ResponseEntity.ok(service.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> update(@RequestBody ServiceDTO serviceDTO, @PathVariable Integer id) {
        Service service = serviceRepository.findById(id).orElse(null);
        if (service == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            service.updateFromDTO(serviceDTO);
            serviceRepository.save(service);
            return ResponseEntity.ok(service.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> delete(@PathVariable Integer id) {
        Service service = serviceRepository.findById(id).orElse(null);
        if (service == null) {
            return ResponseEntity.notFound().build();
        }
        int serviceId = service.getId();
        serviceRepository.delete(service);
        return ResponseEntity.ok(serviceId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> get(@PathVariable Integer id) {
        Service service = serviceRepository.findById(id).orElse(null);
        if (service == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service);
    }
}
