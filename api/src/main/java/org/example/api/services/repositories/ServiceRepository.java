package org.example.api.services.repositories;

import org.example.api.services.data.Service;
import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<Service, Integer> {

}
