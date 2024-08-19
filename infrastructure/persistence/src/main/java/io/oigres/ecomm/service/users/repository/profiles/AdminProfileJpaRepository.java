package io.oigres.ecomm.service.users.repository.profiles;

import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
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
public interface AdminProfileJpaRepository extends SearchRepository<AdminProfile, Long>, AdminProfileRepository {

    @Override
    @Query("SELECT a FROM AdminProfile a WHERE (a.user.id = :userId) AND (a.deletedAt is null)")
    Optional<AdminProfile> findById(@Param("userId") Long userId);

    @Override
    @Query("SELECT a FROM AdminProfile a WHERE (a.deletedAt is null)")
    Page<AdminProfile> findAll(Pageable pageable);

}
