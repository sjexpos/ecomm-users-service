package io.oigres.ecomm.service.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.users.domain.State;
import io.oigres.ecomm.service.users.repository.StateRepository;

@Repository
public interface StateJpaRepository extends SearchRepository<State, Long>, StateRepository {

    @Override
    Page<State> findAll(Pageable pageable);
}
