package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.entities.User;
import com.vubq.joyboystore.repositories.UserRepository;
import com.vubq.joyboystore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByUserName(String userName) {
        return this.userRepository.findByUserName(userName);
    }

    @Override
    public Boolean existsByUserName(String userName) {
        return this.userRepository.existsByUserName(userName);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public User getById(String id) {
        return this.userRepository.findById(id).orElse(null);
    }
}
