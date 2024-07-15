package com.vubq.joyboystore.services;

import com.vubq.joyboystore.entities.Role;
import com.vubq.joyboystore.enums.ERole;

import java.util.Optional;

public interface RoleService {

    public Optional<Role> findByName(ERole name);
}
