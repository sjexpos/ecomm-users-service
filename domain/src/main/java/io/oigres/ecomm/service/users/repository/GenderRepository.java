package io.oigres.ecomm.service.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.users.domain.Gender;

import java.util.Optional;


public interface GenderRepository extends GenericRepository<Gender, Long> {
    Optional<Gender> findById(long id);

    Page<Gender> findAll(Pageable pageable);
}
