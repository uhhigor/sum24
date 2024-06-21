package org.example.api.model;

import org.example.api.exception.ServiceEntityException;
import org.example.api.users.data.User;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ServiceEntityBuilder {
    private String name;
    private String address;
    private User owner;
    private List<String> fields;
    private Class<? extends ServiceEntity> type;

    public ServiceEntityBuilder(Class<? extends ServiceEntity> type) {
        this.type = type;
    }

    public ServiceEntityBuilder setName(String name) {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");
        this.name = name;
        return this;
    }

    public ServiceEntityBuilder setAddress(String address) {
        if(address == null || address.isEmpty())
            throw new IllegalArgumentException("Address cannot be null or empty");
        this.address = address;
        return this;
    }

    public ServiceEntityBuilder setOwner(User owner) {
        if(owner == null)
            throw new IllegalArgumentException("Owner cannot be null");
        this.owner = owner;
        return this;
    }

    public ServiceEntityBuilder setFields(List<String> fields) {
        if(fields == null)
            throw new IllegalArgumentException("Fields cannot be null");
        if(type != ExtendedServiceEntity.class)
            throw new IllegalArgumentException("Fields can only be set for ExtendedServiceEntity");
        this.fields = fields;
        return this;
    }

    public ServiceEntity build() {
        if (type == BasicServiceEntity.class) {
            BasicServiceEntity service = new BasicServiceEntity();
            service.setName(name);
            service.setAddress(address);
            service.setOwner(owner);
            return service;
        } else if (type == ExtendedServiceEntity.class) {
            ExtendedServiceEntity service = new ExtendedServiceEntity();
            service.setName(name);
            service.setAddress(address);
            service.setOwner(owner);
            service.setFields(fields);
            return service;
        } else {
            throw new IllegalArgumentException("Unsupported service entity type: " + type);
        }
    }
}
