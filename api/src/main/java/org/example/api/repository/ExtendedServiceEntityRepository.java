package org.example.api.repository;

import org.example.api.model.BasicServiceEntity;
import org.example.api.model.ExtendedServiceEntity;
import org.springframework.data.repository.CrudRepository;

public interface ExtendedServiceEntityRepository extends CrudRepository<ExtendedServiceEntity, Integer> {

}
