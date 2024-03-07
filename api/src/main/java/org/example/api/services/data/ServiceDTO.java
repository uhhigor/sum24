package org.example.api.services.data;

public class ServiceDTO {
    private String name;
    private String address;

    public boolean isValid() {
        return address != null && !address.isEmpty()
                && name != null && !name.isEmpty();
    }

    public ServiceDTO() {
    }

    public Service toService() throws IllegalArgumentException {
        if(isValid())
            return new Service(name, address);
        throw new IllegalArgumentException("Invalid service data");
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
