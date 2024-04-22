package org.example.api.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.example.api.users.data.User;

@Entity
public class ServiceObject {

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

    public ServiceObject() {
    }

    public ServiceObject(String name, String address, int port, User owner) {
        this.name = name;
        this.address = address;
        this.port = port;
        this.owner = owner;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Integer getPort() {return port;}

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

    public static ServiceBuilder builder() {
        return new ServiceBuilder();
    }

    public static class ServiceBuilder {
        private String name;
        private String address;
        private int port;

        private User owner;

        public ServiceBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ServiceBuilder address(String address) {
            this.address = address;
            return this;
        }

        public ServiceBuilder port(int port) {
            this.port = port;
            return this;
        }

        public ServiceBuilder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public ServiceObject build() {
            return new ServiceObject(name, address, port, owner);
        }
    }

    public static class ServiceObjectDTO {
        private String name;
        private String address;
        private int port;
        public ServiceObjectDTO(String name, String address, int port) {
            this.name = name;
            this.address = address;
            this.port = port;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public int getPort() {
            return port;
        }
    }
}
