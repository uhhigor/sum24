package org.example.api.services.data;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.example.api.users.data.User;

@Entity
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JsonBackReference
    @JsonIgnore
    private User owner;

    private String name;

    private String address;

    private int port;

    public Service() {
    }

    public Service(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {return port;}

    public User getOwner() {return owner;}

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(int port) {this.port = port;}

    public void setOwner(User user) {this.owner = user;}

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", address='" + address + '\'' +
                ", port='" + port + '\'' +
                '}';
    }


    public void updateFromDTO(ServiceDTO serviceDTO) {
        if(serviceDTO.isValid()) {
            this.name = serviceDTO.getName();
            this.address = serviceDTO.getAddress();
            this.port = serviceDTO.getPort();
        } else {
            throw new IllegalArgumentException("Invalid service data");
        }
    }
}
