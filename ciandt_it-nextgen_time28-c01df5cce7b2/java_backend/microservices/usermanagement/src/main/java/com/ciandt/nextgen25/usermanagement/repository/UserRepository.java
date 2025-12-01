package com.ciandt.nextgen25.usermanagement.repository;

import java.util.Optional;
import java.util.List;
import com.ciandt.nextgen25.usermanagement.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ciandt.nextgen25.usermanagement.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByTypeNotAndActiveTrue(UserType type);
    
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("SELECT u FROM User u WHERE u.pdm.id = :pdmId AND u.active = true")
    List<User> findByPdmId(@Param("pdmId") Long pdmId);
    
} 



