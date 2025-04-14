package com.example.user_service.controller;


import com.example.user_service.models.UserDetailsWithGroupsAndRoles;
import com.example.user_service.service.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;



    @PostMapping
    public void createUser(@RequestBody Map<String, String> userDetails) {
        userService.createUser(
                userDetails.get("username"),
                userDetails.get("email"),
                userDetails.get("firstName"),
                userDetails.get("lastName"),
                userDetails.get("password"),
                userDetails.get("roleName")


        );
    }

    @PostMapping("/assign")
    public
    ResponseEntity<String> assignRole(@RequestParam String userId, @RequestParam String roleName) {
        userService.assignRoleToUser(userId, roleName);
        return ResponseEntity.ok("Role assigned to user");
    }


    @GetMapping("/{id}")
    public UserDetailsWithGroupsAndRoles getUser(@PathVariable String id) {
        return userService.getUserDetailsWithGroupsAndRoles(id);
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        userService.updateUser(id, updates);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public
    List<UserRepresentation> listUsers() {
        return userService.listUsers();
    }


    @PostMapping("/{userId}/groups")
    public void assignUserToGroup(@PathVariable String userId, @RequestParam String groupName) {
        userService.assignUserToGroup(userId, groupName);
    }

    @GetMapping("/group-role")
    public List<UserRepresentation> getUsersByGroupAndRole(@RequestParam String groupName, @RequestParam String roleName) {
        return userService.getUsersByGroupAndRole(groupName, roleName);
    }
    @GetMapping("/get")
    public String home() {
        return "<h2>Microservice two is running</h2>";}

}
