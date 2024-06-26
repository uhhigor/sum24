package org.example.api.controller;


import org.example.api.Scheduler;
import org.example.api.users.data.AuthenticationResponse;
import org.example.api.users.data.User;
import org.example.api.users.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    private final Scheduler scheduler;

    public AuthenticationController(AuthenticationService authenticationService, Scheduler scheduler) {
        this.authenticationService = authenticationService;
        this.scheduler = scheduler;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User request) {
        AuthenticationResponse ar = authenticationService.register(request);
        
        if (ar.getToken() != null) {
            return ResponseEntity.ok(ar);
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ar);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User request) {
        AuthenticationResponse ar = authenticationService.login(request);
        scheduler.setToken(ar.getToken());
        if (ar.getToken() != null) {
            return ResponseEntity.ok(ar);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ar);
        }
    }

   @GetMapping("/login/{userName}")
    public ResponseEntity<Integer> getUserId(@PathVariable String userName) {
        User user = authenticationService.getUserId(userName);
        scheduler.setEmail(userName);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
       scheduler.setUserId(user.getId());
       return ResponseEntity.ok(user.getId());
    }

    @DeleteMapping ("/user/{userName}")
    public ResponseEntity<Integer> deleteUser(@PathVariable String userName) {
        authenticationService.deleteUser(userName);
        return (ResponseEntity<Integer>) ResponseEntity.ok();
    }

}
