package io.oigres.ecomm.service.users.repository.profiles;

import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.repository.SearchRepository;
import io.oigres.ecomm.service.users.repository.profiles.ConsumerProfileRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ConsumerProfileJpaRepository extends SearchRepository<ConsumerProfile, Long>, ConsumerProfileRepository {
    @Override
    @Query("SELECT c FROM ConsumerProfile c WHERE (c.user.id = :userId) AND (c.deletedAt is null)")
    Optional<ConsumerProfile> findById(@Param("userId") Long userId);

    @Override
    @Query("SELECT c FROM ConsumerProfile c WHERE (c.deletedAt is null)")
    Page<ConsumerProfile> findAll(Pageable pageable);

    @Override
    @Query("SELECT c FROM ConsumerProfile c WHERE (c.deletedAt is null) AND (c.card.cardImage.imageURL = :cardImageUrl)")
    Optional<ConsumerProfile> findByCardImageUrl(@Param("cardImageUrl") String cardImageUrl);
}
