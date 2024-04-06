package org.example.api.users.data;

public class AuthenticationResponse {
    private String token;
    private String message;
    private int status;

    public AuthenticationResponse(String token) {
        this.token = token;
        this.message = "OK";
    }
    
    public AuthenticationResponse(int status) {
        this.token = null;
        
        if (status == 409) {
            this.message = "User already exist";
        } else if (status == 404) {
            this.message = "Incorrect username or password";
        }
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }
}
