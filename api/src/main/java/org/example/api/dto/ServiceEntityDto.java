package org.example.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.api.model.ExtendedServiceEntity;
import org.example.api.model.ServiceEntity;

import java.util.ArrayList;
import java.util.List;

public class ServiceEntityDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        public String message;
        public Data service;

        public Response(String message, ServiceEntity entity) {
            this.message = message;
            this.service = new Data(entity);
        }

        public Response(String message) {
            this.message = message;
        }

        public Response(ServiceEntity entity) {
            this.service = new Data(entity);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ListResponse {
        public List<Data> services;
        public ListResponse(List<ServiceEntity> entities) {
            services = new ArrayList<>();
            entities.forEach(entity -> services.add(new Data(entity)));
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Data {
        public Integer id;
        public String name;
        public String address;
        public Integer userId;
        public List<String> fields;

        public Data(ServiceEntity entity) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.address = entity.getAddress();
            this.userId = entity.getOwner().getId();
            if(entity instanceof ExtendedServiceEntity extendedEntity) {
                this.fields = extendedEntity.getFields();
            }
        }
    }

    public static class Request {
        public String name;
        public String address;
        public List<String> fields; // Only for ExtendedServiceEntity - may be null
    }
}
