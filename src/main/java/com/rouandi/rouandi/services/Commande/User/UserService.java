package com.rouandi.rouandi.services.Commande.User;

import com.rouandi.rouandi.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
@Transactional
@Service
public interface UserService {
//    public List<User> ListUsers();
    public void updateUser(Long id, User updatedData) ;
    public User addUser(User user);
    public void deleteUser(Long id);
  /*  public void removeUser(Long id);
    public void editUser(Long id);
*/
}
