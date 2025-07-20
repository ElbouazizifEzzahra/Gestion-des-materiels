package com.rouandi.rouandi.controllers;

import com.rouandi.rouandi.DTOs.UserDTO.UserMapper;
import com.rouandi.rouandi.DTOs.UserDTO.UserRequestDTO;
import com.rouandi.rouandi.DTOs.UserDTO.UserResponseDTO;
import com.rouandi.rouandi.entities.Role;
import com.rouandi.rouandi.entities.User;
import com.rouandi.rouandi.services.Commande.User.EmailService;
import com.rouandi.rouandi.services.Commande.User.UserService;
import com.rouandi.rouandi.services.Query.User.UserServiceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private UserServiceQuery userServiceQuery;


    public UserController(UserService userService, UserServiceQuery userServiceQuery) {
        this.userService = userService;
        this.userServiceQuery = userServiceQuery;
    }

    @PostMapping("/add")
    public ResponseEntity<UserResponseDTO>ajouterUser(@RequestBody UserRequestDTO userRequestDTO){
        User user= UserMapper.fromDTO(userRequestDTO);
      User saved=userService.addUser(user);
        return ResponseEntity.ok(UserMapper.toDTO(saved));
    }
    @PostMapping("/users")
    public ResponseEntity<List>AllUsers(){
     return  ResponseEntity.ok(userServiceQuery.users());
    }

    @PostMapping("/filterByRole")
    public ResponseEntity<List>filterByRole(Role role){
        return  ResponseEntity.ok(userServiceQuery.filterByRole(role));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message","bien supprimé"));
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser
    ) {
        userService.updateUser(id, updatedUser);
        return ResponseEntity.ok("Utilisateur mis à jour avec succès !");
    }
    }
