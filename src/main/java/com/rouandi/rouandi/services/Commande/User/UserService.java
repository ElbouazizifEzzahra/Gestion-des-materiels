package com.rouandi.rouandi.services.Commande.User;

import com.rouandi.rouandi.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Transactional
@Service
public interface UserService {
//    public List<User> ListUsers();
    public void updateUser(Long id, User updatedData) throws AccessDeniedException;
    public User addUser(User user);
    public Optional<User> findUser(Long id);
    public void deleteUser(Long id);
    public void updateUserById(Long id, User updatedData) ;
  /*  public void removeUser(Long id);
    public void editUser(Long id);*/
    public void changeUserPassword(Long userId,
                                   String currentPassword,
                                   String newPassword,
                                   String currentUsername);

}
