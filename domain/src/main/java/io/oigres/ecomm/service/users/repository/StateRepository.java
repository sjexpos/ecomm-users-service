package io.oigres.ecomm.service.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.users.domain.State;

import java.util.List;
import java.util.Optional;

public interface StateRepository extends GenericRepository<State, Long> {
    Optional<State> findById(long id);

    List<State> findAllByCountryId(long countryId);

    Page<State> findAll(Pageable pageable);
}
