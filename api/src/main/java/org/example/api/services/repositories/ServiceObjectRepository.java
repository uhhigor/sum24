package org.example.api.services.repositories;

import org.example.api.services.data.ServiceObject;
import org.springframework.data.repository.CrudRepository;

public interface ServiceObjectRepository extends CrudRepository<ServiceObject, Integer> {

}
