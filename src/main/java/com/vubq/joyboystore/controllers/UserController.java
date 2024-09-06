package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.UserInfoDto;
import com.vubq.joyboystore.entities.Order;
import com.vubq.joyboystore.entities.Role;
import com.vubq.joyboystore.entities.User;
import com.vubq.joyboystore.enums.EOrderType;
import com.vubq.joyboystore.enums.ERole;
import com.vubq.joyboystore.services.RoleService;
import com.vubq.joyboystore.services.UserService;
import com.vubq.joyboystore.utils.DataTableRequest;
import com.vubq.joyboystore.utils.DataTableResponse;
import com.vubq.joyboystore.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/webapi/user")
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @GetMapping("/get-user-info")
    public Response getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = this.userService.findByUserName(userDetails.getUsername()).orElse(null);
            if (user != null) {
                return Response.build().ok().data(user);
            }
        }
        return Response.build().code(401);
    }

    @GetMapping("get-all-page")
    public DataTableResponse getAllPage(DataTableRequest request, @RequestParam String type, @RequestParam String status) {
        Page<User> result = this.userService.getAllPage(request, type, status);

        return DataTableResponse.build()
                .ok()
                .totalRows(result.getTotalElements())
                .items(result.get().toList());
    }

    @GetMapping("get-all-role")
    public Response getAllRole() {
        return Response.build().ok().data(this.roleService.findAll());
    }

    @PostMapping("create")
    private Response create(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userService.save(user);
        return Response.build().ok();
    }

    @PostMapping("update")
    private Response update(@RequestBody User user) {
        this.userService.save(user);
        return Response.build().ok();
    }

    @GetMapping("get-by-id/{id}")
    private Response getById(@PathVariable String id) {
        return Response.build().ok().data(this.userService.findById(id));
    }

    @PostMapping("register")
    private Response register(@RequestBody User user) {
        Set<Role> roles = new HashSet<>();
        roles.add(this.roleService.findByName(ERole.ROLE_CUSTOMER).get());
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userService.save(user);
        return Response.build().ok();
    }

    @PostMapping("update-user")
    private Response updateUser(@RequestBody User user) {
        User user1 = this.userService.getById(this.getTheCurrentlyLoggedInUserId());
        user1.setFullName(user.getFullName());
        user1.setPhoneNumber(user.getPhoneNumber());
        user1.setEmail(user.getEmail());
        user1.setCity(user.getCity());
        user1.setDistrict(user.getDistrict());
        user1.setWard(user.getWard());
        user1.setCityCode(user.getCityCode());
        user1.setDistrictCode(user.getDistrictCode());
        user1.setWardCode(user.getWardCode());
        user1.setAddress(user.getAddress());
        this.userService.save(user);
        return Response.build().ok();
    }
}
