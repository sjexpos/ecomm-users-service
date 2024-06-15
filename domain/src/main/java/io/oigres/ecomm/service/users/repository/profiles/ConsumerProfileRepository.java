package io.oigres.ecomm.service.users.repository.profiles;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.repository.GenericRepository;

import java.util.Optional;

public interface ConsumerProfileRepository extends GenericRepository<ConsumerProfile, Long> {

    Optional<ConsumerProfile> findById(Long userId);

    Optional<ConsumerProfile> findByUserEmail(String email);

    Page<ConsumerProfile> findAll(Pageable pageable);

    ConsumerProfile save(ConsumerProfile consumerProfile);

    Boolean existsByProfileImage_ImageURL(String imageUrl);

    Optional<ConsumerProfile> findByCardImageUrl(String cardImageUrl);
    
}
