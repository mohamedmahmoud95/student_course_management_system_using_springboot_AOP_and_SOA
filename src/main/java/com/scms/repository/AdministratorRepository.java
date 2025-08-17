package com.scms.repository;

import com.scms.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    
    Optional<Administrator> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT a FROM Administrator a WHERE a.email = :email AND a.password = :password")
    Optional<Administrator> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}
