package io.oigres.ecomm.service.users.repository.profiles;

import io.oigres.ecomm.service.users.domain.Profile;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.repository.SearchRepository;
import io.oigres.ecomm.service.users.repository.profiles.ProfileRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Repository
@Transactional
public interface ProfileJpaRepository extends SearchRepository<Profile, Long>, ProfileRepository{
    @Override
    @Query("SELECT p FROM Profile p WHERE (p.user.id = :userId) AND (p.profileType.profile = :profileType) AND (p.deletedAt is null)")
    Optional<Profile> findByIdAndProfileType(@Param("userId") Long userId, @Param("profileType") ProfileTypeEnum profileType);

    @Query(value = "SELECT p.* FROM profiles p LEFT JOIN profile_images pi ON (p.profile_image_id = pi.id) WHERE p.id <> :profileId AND pi.image_url = :imageUrl ", nativeQuery = true)
    List<Profile> existsByNotIdAndProfileImage_ImageURL(@Param("profileId") Long id, @Param("imageUrl") String imageUrl);

}
