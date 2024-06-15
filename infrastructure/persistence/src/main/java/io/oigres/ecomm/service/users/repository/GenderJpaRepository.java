package io.oigres.ecomm.service.users.repository;

import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.users.domain.Gender;
import io.oigres.ecomm.service.users.repository.GenderRepository;


@Repository
public interface GenderJpaRepository extends SearchRepository<Gender, Long>,  GenderRepository {
}
