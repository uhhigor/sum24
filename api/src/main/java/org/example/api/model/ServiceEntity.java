package org.example.api.model;

import org.example.api.users.data.User;

public interface ServiceEntity {
    Integer getId();
    String getName();
    String getAddress();
    User getOwner();
    void setName(String name);
    void setAddress(String address);
    void setOwner(User user);

    void setId(Integer id);
}
