package com.vinceadamo.dataapi.dataapi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.vinceadamo.dataapi.dataapi.entities.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findOneByEmail(String email);
}