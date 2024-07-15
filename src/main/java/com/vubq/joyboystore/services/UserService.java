package com.vubq.joyboystore.services;

import com.vubq.joyboystore.entities.User;

import java.util.Optional;

public interface UserService {

    public Optional<User> findByUserName(String userName);

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

    User getById(String id);
}
