package com.rouandi.rouandi.repositories;

import com.rouandi.rouandi.entities.Role;
import com.rouandi.rouandi.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Transactional
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsUserById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
   void deleteById(Long id);
    List<User> findByRole(Role role);

    List<User> findAll();
    Optional<User>findUserByEmail(String email);
    boolean existsUserByEmail(String email);
    @Modifying
    @Query("UPDATE User u SET u.code = :code, u.email = :email, u.fonctionalite = :fonctionalite, u.nom = :nom, u.NumeroTelephone = :numeroTelephone, u.prenom = :prenom, u.projet = :projet, u.role = :role, u.site = :site, u.societe = :societe WHERE u.id = :id")
    void updateUser(
            @Param("id") Long id,
            @Param("code") String code,
            @Param("email") String email,
            @Param("fonctionalite") String fonctionalite,
            @Param("nom") String nom,
            @Param("numeroTelephone") String numeroTelephone,
            @Param("prenom") String prenom,
            @Param("projet") String projet,
            @Param("role") String role,
            @Param("site") String site,
            @Param("societe") String societe
    );

}
