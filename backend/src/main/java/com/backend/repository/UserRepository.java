package com.backend.repository;

import com.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u")
    Collection<User> allUsers();

    Optional<User> findByEmail(String email);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.firstName = :firstName WHERE u.id = :id")
    int updateFirstName(@Param("id") Integer id, @Param("firstName") String firstName);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastName = :lastName WHERE u.id = :id")
    int updateLastName(@Param("id") Integer id, @Param("lastName") String lastName);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.email = :email WHERE u.id = :id")
    int updateEmail(@Param("id") Integer id, @Param("email") String email);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    int updatePassword(@Param("id") Integer id, @Param("password") String password);
    
}
