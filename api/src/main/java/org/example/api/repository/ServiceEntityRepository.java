package org.example.api.repository;

import org.example.api.model.ServiceEntity;
import org.springframework.data.repository.CrudRepository;

public interface ServiceEntityRepository extends CrudRepository<ServiceEntity, Integer> {

}
