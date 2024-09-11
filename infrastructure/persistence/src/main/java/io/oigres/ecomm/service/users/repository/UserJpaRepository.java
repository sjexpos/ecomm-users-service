package io.oigres.ecomm.service.users.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.users.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends SearchRepository<User, Long>, UserRepository {
    
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
    Optional<User> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    @EntityGraph(attributePaths = {"profiles"})
    List<User> findAll();

}
