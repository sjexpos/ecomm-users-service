package io.oigres.ecomm.service.users.repository.profiles;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.repository.GenericRepository;

import java.util.Optional;

public interface DispensaryProfileRepository extends GenericRepository<DispensaryProfile, Long> {
    Optional<DispensaryProfile> findById(Long userId);
    Optional<DispensaryProfile> findByDispensaryId(Long dispensaryId);
    Page<DispensaryProfile> findAll(Pageable pageable);
    DispensaryProfile save(DispensaryProfile dispensaryProfile);
    void deleteByUserId(Long userId);
}
