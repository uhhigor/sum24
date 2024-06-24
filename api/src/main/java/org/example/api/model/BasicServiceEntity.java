package org.example.api.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.example.api.users.data.User;

@Entity
public class BasicServiceEntity implements ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JsonBackReference
    @JsonIgnore
    private User owner;

    private String name;

    private String address;

    public BasicServiceEntity() {
    }

    public BasicServiceEntity(String name, String address, User owner) {
        this.name = name;
        this.address = address;
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

    public User getOwner() {return owner;}

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOwner(User user) {this.owner = user;}

    public void setId(Integer id) {
        this.id = id;
    }
}
