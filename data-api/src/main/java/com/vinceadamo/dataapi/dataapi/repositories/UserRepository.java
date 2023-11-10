package com.vinceadamo.dataapi.dataapi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.vinceadamo.dataapi.dataapi.entities.User;
import java.util.UUID;



public interface UserRepository extends CrudRepository<User, UUID> {
    User findOneByEmail(String email);
}