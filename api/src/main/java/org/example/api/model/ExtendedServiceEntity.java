package org.example.api.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import org.example.api.users.data.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
public class ExtendedServiceEntity extends BasicServiceEntity {

    @ElementCollection
    private List<String> fields = new ArrayList<>();

    public ExtendedServiceEntity(String name, String address, User owner) {
        super(name, address, owner);
    }

    public ExtendedServiceEntity() {
    }

    public List<String> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public void addField(String field) {
        fields.add(field);
    }
}
