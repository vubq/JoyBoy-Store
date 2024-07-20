package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.UserInfoDto;
import com.vubq.joyboystore.entities.User;
import com.vubq.joyboystore.services.UserService;
import com.vubq.joyboystore.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/webapi/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/info")
    public Response getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = this.userService.findByUserName(userDetails.getUsername()).orElse(null);
            if (user != null) {
                return Response.build().ok().data(
                        UserInfoDto.builder()
                                .userId(user.getId())
                                .roles(user.getRoles().stream().map(role -> role.getName().toString()).collect(Collectors.toList()))
                                .name(user.getFullName())
                                .avatar(user.getAvatar())
                                .introduction("")
                                .build()
                );
            }
        }
        return Response.build().code(401);
    }
}
