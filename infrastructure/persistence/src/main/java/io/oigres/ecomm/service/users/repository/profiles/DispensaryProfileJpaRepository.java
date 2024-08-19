package io.oigres.ecomm.service.users.repository.profiles;

import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.repository.SearchRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface DispensaryProfileJpaRepository extends SearchRepository<DispensaryProfile, Long>, DispensaryProfileRepository {
    @Override
    @Query("SELECT d FROM DispensaryProfile d WHERE (d.user.id = :userId) AND (d.deletedAt is null)")
    Optional<DispensaryProfile> findById(@Param("userId") Long userId);

    @Override
    @Query("SELECT d FROM DispensaryProfile d WHERE (d.dispensaryId = :dispensaryId) AND (d.deletedAt is null)")
    Optional<DispensaryProfile> findByDispensaryId(@Param("dispensaryId") Long dispensaryId);

    @Override
    @Query("SELECT d FROM DispensaryProfile d WHERE (d.deletedAt is null)")
    Page<DispensaryProfile> findAll(Pageable pageable);

}
