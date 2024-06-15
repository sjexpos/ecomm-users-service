package io.oigres.ecomm.service.users.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.users.domain.User;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends SearchRepository<User, Long>, UserRepository {
    
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
    Optional<User> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    @Override
    @Modifying
    @Query("UPDATE User u set u.deletedAt = now() WHERE u.id = :userId")
    void deleteById(@Param("userId") Long userId);
}
