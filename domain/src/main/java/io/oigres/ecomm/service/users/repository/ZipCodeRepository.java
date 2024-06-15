package io.oigres.ecomm.service.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.users.domain.ZipCode;

import java.util.List;
import java.util.Optional;

public interface ZipCodeRepository extends GenericRepository<ZipCode, Long> {
    Optional<ZipCode> findById(long id);

    Page<ZipCode> findAllByStateId(long stateId, Pageable pageable);

    List<ZipCode> findAll();
}
