package org.example.api.model;

import org.example.api.exception.ServiceEntityException;
import org.example.api.users.data.User;

import java.lang.reflect.InvocationTargetException;

public class ServiceEntityBuilder {
    private String name;
    private String address;
    private User owner;
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

    public ServiceEntity build() throws ServiceEntityException {
        if(name == null || address == null || owner == null)
            throw new IllegalArgumentException("Name, address and owner must be set");
        ServiceEntity serviceEntity = null;
        try {
            serviceEntity = type.getDeclaredConstructor().newInstance();
            serviceEntity.setName(name);
            serviceEntity.setAddress(address);
            serviceEntity.setOwner(owner);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ServiceEntityException("Error creating service entity", e);
        }
        return serviceEntity;
    }
}
