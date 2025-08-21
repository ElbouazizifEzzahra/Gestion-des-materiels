package com.rouandi.rouandi.services.Commande.User;

import com.rouandi.rouandi.entities.Role;
import com.rouandi.rouandi.entities.User;
import com.rouandi.rouandi.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.Random;

@Transactional
@Service
public class UserServiceImp implements UserService {
    private final  UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    private static final String UPPERCASES ="QWERTYUIOPASDFGHJKLZXCVBNM";
    private static final String LOWERCASES="qwertyuiopasdfghjklzxcvbnm";
    private static final String CHIFFRES="1234567890";
    private static final String SYMBOLS="!@#$%^&*()_-+=<>?";
    public static final String ALL_CHARS=UPPERCASES+LOWERCASES+SYMBOLS+CHIFFRES;

    @Autowired
    EmailService emailService;
    public UserServiceImp(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public User addUser(User user) {
      String password= Password();
        user.setPassword(password);
        emailService.sendEmail("elbouazizi.fat.fst@uhp.ac.ma",user.getEmail()," YOUR PASSWORD","your password is : "+user.getPassword()+" "+ "Please log in using this password and make sure to change it within 24 hours.");
      user.setPassword(encoder.encode(password));

      return userRepository.findUserByEmail(user.getEmail())
                .orElseGet(()->userRepository.save(user));

    }

    @Override
    public Optional<User> findUser(Long id) {

        Optional<User> user= userRepository.findById(id);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        if(userRepository.existsUserById(id))
            userRepository.deleteById(id);
        else
            System.out.println("l'utilisateur que vous avez choisi n'existe pas ");

    }



    public String Password(){
        StringBuilder passwordBuilder= new StringBuilder();
        int length=8;
        Random r = new Random();
        passwordBuilder.append(UPPERCASES.charAt(r.nextInt(UPPERCASES.length())));
        passwordBuilder.append(LOWERCASES.charAt(r.nextInt(LOWERCASES.length())));
        passwordBuilder.append(CHIFFRES.charAt(r.nextInt(CHIFFRES.length())));
        passwordBuilder.append(SYMBOLS.charAt(r.nextInt(SYMBOLS.length())));
        for (int i = 4; i < length; i++) {
            passwordBuilder.append(ALL_CHARS.charAt(r.nextInt(ALL_CHARS.length())));
        }

        return shuffleString(passwordBuilder.toString());
    }

    private String shuffleString(String input) {
        char[] chars = input.toCharArray();
        Random r = new Random();
        for (int i = 0; i < chars.length; i++) {
            int j = r.nextInt(chars.length);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String email;
        Long id;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();

        } else {
            email = principal.toString();
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email));
    }
    /*@Override
    @Transactional
    public void updateUser(Long id, User updatedData) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec ID : " + id));

        if (updatedData.getCode() != null)
            existingUser.setCode(updatedData.getCode());

        if (updatedData.getEmail() != null)
            existingUser.setEmail(updatedData.getEmail());

        if (updatedData.getFonctionalite() != null)
            existingUser.setFonctionalite(updatedData.getFonctionalite());

        if (updatedData.getNom() != null)
            existingUser.setNom(updatedData.getNom());

        if (updatedData.getNumeroTelephone() != null)
            existingUser.setNumeroTelephone(updatedData.getNumeroTelephone());

        if (updatedData.getPrenom() != null)
            existingUser.setPrenom(updatedData.getPrenom());

        if (updatedData.getProjet() != null)
            existingUser.setProjet(updatedData.getProjet());

        if (updatedData.getRole() != null)
            existingUser.setRole(updatedData.getRole());

        if (updatedData.getSite() != null)
            existingUser.setSite(updatedData.getSite());

        if (updatedData.getSociete() != null)
            existingUser.setSociete(updatedData.getSociete());

        userRepository.save(existingUser);
    }
*/
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
    }

    @Override
    @Transactional
    public void updateUser(Long id, User updatedData) throws AccessDeniedException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec ID : " + id));

        User currentUser = getAuthenticatedUser();
        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);

        if (!isAdmin && !currentUser.getId().equals(id)) {
            throw new AccessDeniedException("Vous ne pouvez modifier que vos propres informations.");
        }

        if (isAdmin) {
            if (updatedData.getCode() != null)
                existingUser.setCode(updatedData.getCode());
            if (updatedData.getEmail() != null)
                existingUser.setEmail(updatedData.getEmail());
            if (updatedData.getFonctionalite() != null)
                existingUser.setFonctionalite(updatedData.getFonctionalite());
            if (updatedData.getNom() != null)
                existingUser.setNom(updatedData.getNom());
            if (updatedData.getNumeroTelephone() != null)
                existingUser.setNumeroTelephone(updatedData.getNumeroTelephone());
            if (updatedData.getPrenom() != null)
                existingUser.setPrenom(updatedData.getPrenom());
            if (updatedData.getProjet() != null)
                existingUser.setProjet(updatedData.getProjet());
            if (updatedData.getRole() != null)
                existingUser.setRole(updatedData.getRole());
            if (updatedData.getSite() != null)
                existingUser.setSite(updatedData.getSite());
            if (updatedData.getSociete() != null)
                existingUser.setSociete(updatedData.getSociete());
            if (updatedData.getPassword() != null && !updatedData.getPassword().isBlank()) {
                existingUser.setPassword(encoder.encode(updatedData.getPassword()));
            }
        } else if(!isAdmin) {
            if (updatedData.getNom() != null)
                existingUser.setNom(updatedData.getNom());
            if (updatedData.getPrenom() != null)
                existingUser.setPrenom(updatedData.getPrenom());
            if (updatedData.getNumeroTelephone() != null)
                existingUser.setNumeroTelephone(updatedData.getNumeroTelephone());
            if (updatedData.getPassword() != null && !updatedData.getPassword().isBlank()) {
                existingUser.setPassword(encoder.encode(updatedData.getPassword()));
            }

        }

        userRepository.save(existingUser);
    }
    @Override
    @Transactional
    public void updateUserById(Long id, User updatedData) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec ID : " + id));

            if (updatedData.getCode() != null)
                existingUser.setCode(updatedData.getCode());
            if (updatedData.getEmail() != null)
                existingUser.setEmail(updatedData.getEmail());
            if (updatedData.getFonctionalite() != null)
                existingUser.setFonctionalite(updatedData.getFonctionalite());
            if (updatedData.getNom() != null)
                existingUser.setNom(updatedData.getNom());
            if (updatedData.getNumeroTelephone() != null)
                existingUser.setNumeroTelephone(updatedData.getNumeroTelephone());
            if (updatedData.getPrenom() != null)
                existingUser.setPrenom(updatedData.getPrenom());
            if (updatedData.getProjet() != null)
                existingUser.setProjet(updatedData.getProjet());
            if (updatedData.getRole() != null)
                existingUser.setRole(updatedData.getRole());
            if (updatedData.getSite() != null)
                existingUser.setSite(updatedData.getSite());
            if (updatedData.getSociete() != null)
                existingUser.setSociete(updatedData.getSociete());
            if (updatedData.getPassword() != null && !updatedData.getPassword().isBlank()) {
                existingUser.setPassword(encoder.encode(updatedData.getPassword()));
            }
        userRepository.save(existingUser);
        }


    public void changeUserPassword(Long userId,
                                   String currentPassword,
                                   String newPassword,
                                   String currentUsername) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ExpressionException("Utilisateur non trouvé"));

        if (!encoder.matches(currentPassword, user.getPassword())) {
            throw new BadCredentialsException("Mot de passe actuel incorrect");
        }

        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères");
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }
    }



