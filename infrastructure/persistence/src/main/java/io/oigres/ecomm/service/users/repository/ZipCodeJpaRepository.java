package io.oigres.ecomm.service.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.users.domain.ZipCode;
import io.oigres.ecomm.service.users.repository.ZipCodeRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZipCodeJpaRepository extends SearchRepository<ZipCode, Long>, ZipCodeRepository {
    @Override
    Optional<ZipCode> findById(long id);
    @Override
    Page<ZipCode> findAllByStateId(long stateId, Pageable pageable);

    @Override
    List<ZipCode> findAll();
}
