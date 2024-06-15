package io.oigres.ecomm.service.users.repository;

import java.util.List;
import java.util.Optional;

import io.oigres.ecomm.service.users.domain.Country;

public interface CountryRepository extends GenericRepository<Country, Long> {
    Optional<Country> findById(long id);

    List<Country> findAll();
}
