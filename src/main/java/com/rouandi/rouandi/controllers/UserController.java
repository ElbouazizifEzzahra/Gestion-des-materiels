package com.rouandi.rouandi.controllers;

import com.rouandi.rouandi.DTOs.UserDTO.PasswordChangeDTO;
import com.rouandi.rouandi.DTOs.UserDTO.UserMapper;
import com.rouandi.rouandi.DTOs.UserDTO.UserRequestDTO;
import com.rouandi.rouandi.DTOs.UserDTO.UserResponseDTO;
import com.rouandi.rouandi.entities.Role;
import com.rouandi.rouandi.entities.User;
import com.rouandi.rouandi.services.Commande.User.UserService;
import com.rouandi.rouandi.services.Query.User.UserServiceQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private UserService userService;
    private UserServiceQuery userServiceQuery;


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<UserResponseDTO> ajouterUser(@RequestBody UserRequestDTO userRequestDTO) {
        User user = UserMapper.fromDTO(userRequestDTO);
        User saved = userService.addUser(user);
        return ResponseEntity.ok(UserMapper.toDTO(saved));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List> AllUsers() {
        return ResponseEntity.ok(userServiceQuery.users());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/filterByRole/{role}")
    public ResponseEntity<List> filterByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userServiceQuery.filterByRole(role));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "bien supprimé"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser
    ) throws AccessDeniedException {
        userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(Map.of("message", "Utilisateur mis à jour avec succès"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUserById(
            @PathVariable Long id,
            @RequestBody User updatedUser
    ) throws AccessDeniedException {
        userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(Map.of("message", "Utilisateur mis à jour avec succès"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> user(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUser(id));
    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<?> changePassword(
            @PathVariable Long userId,
            @RequestBody PasswordChangeDTO passwordChangeDTO,
            Principal principal) {

        try {
            userService.changeUserPassword(userId,
                    passwordChangeDTO.getCurrentPassword(),
                    passwordChangeDTO.getNewPassword(),
                    principal.getName());
            return ResponseEntity.ok().build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Mot de passe actuel incorrect");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}