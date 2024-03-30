package org.example.api.controllers;

import org.example.api.services.repositories.ServiceRepository;
import org.example.api.services.data.Service;
import org.example.api.services.data.ServiceDTO;
import org.example.api.users.data.User;
import org.example.api.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/services")
public class ServiceController {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Iterable<Service>> getAllUser(@PathVariable Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(StreamSupport.stream(serviceRepository.findAll().spliterator(), false)
                .filter(service -> service.getOwner().equals(user))
                .collect(Collectors.toList()));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Integer> add(@PathVariable Integer userId, @RequestBody ServiceDTO serviceDTO) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null)
            return ResponseEntity.notFound().build();
        try {
            Service service = serviceDTO.toService();

            service.setOwner(user);
            serviceRepository.save(service);

            user.addService(service);
            userRepository.save(user);
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

    @GetMapping("/")
    public @ResponseBody Iterable<Service> getAll() {
        return serviceRepository.findAll();
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
