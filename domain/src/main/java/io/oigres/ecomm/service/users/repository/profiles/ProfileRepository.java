package io.oigres.ecomm.service.users.repository.profiles;

import java.util.Optional;

import io.oigres.ecomm.service.users.domain.Profile;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.repository.GenericRepository;

import java.util.List;

public interface ProfileRepository extends GenericRepository<Profile, Long> {

    Optional<Profile> findByIdAndProfileType(Long userId, ProfileTypeEnum profileType);

    Profile save(Profile profile);

    List<Profile> existsByNotIdAndProfileImage_ImageURL(Long id, String imageUrl);

}
