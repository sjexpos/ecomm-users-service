package io.oigres.ecomm.service.users.repository;

import java.util.Optional;

import io.oigres.ecomm.service.users.domain.ProfileImage;

public interface ProfileImageRepository extends GenericRepository<ProfileImage, Long> {
    Optional<ProfileImage> findByImageURL(String imageURL);
    ProfileImage save(ProfileImage profileImage);
    boolean existsByImageURL(String imageURL);
}
