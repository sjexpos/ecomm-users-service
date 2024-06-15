package io.oigres.ecomm.service.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.users.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    User save(User user);

    List<User> findAll();

    Page<User> findAllByDeletedAtIsNull(Pageable pageable);

    long countAllByDeletedAtIsNull();

    void deleteById(Long userId);
}
