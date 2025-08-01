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

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
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
    @GetMapping("/users")
    public ResponseEntity<List>AllUsers(){
     return  ResponseEntity.ok(userServiceQuery.users());
    }

    @GetMapping("/filterByRole/{role}")
    public ResponseEntity<List>filterByRole(@PathVariable Role role){
        return  ResponseEntity.ok(userServiceQuery.filterByRole(role));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message","bien supprimé"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser
    ) throws AccessDeniedException {
        userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(Map.of("message", "Utilisateur mis à jour avec succès"));
    }

    }
