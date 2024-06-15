package io.oigres.ecomm.service.users.usecases.users.images;

import io.oigres.ecomm.service.users.domain.ProfileImage;
import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;
import io.oigres.ecomm.service.users.repository.ProfileImageRepository;
import io.oigres.ecomm.service.users.usecases.users.images.model.UpdateProfileImageDTO;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ChangeProfileImageStatusUseCaseImpl implements ChangeProfileImageStatusUseCase {
    private final ProfileImageRepository profileImageRepository;

    public ChangeProfileImageStatusUseCaseImpl(ProfileImageRepository profileImageRepository) {
        this.profileImageRepository = profileImageRepository;
    }

    @Override
    public UpdateProfileImageDTO handle(String imageUrl) {
        ProfileImage profileImage;

        Optional<ProfileImage> optional = profileImageRepository.findByImageURL(imageUrl);

        if(optional.isPresent()) {
            profileImage = optional.get();
            profileImage.setStatus(ResourceStatusEnum.UPLOADED);
        } else {
            profileImage = ProfileImage.builder()
                    .status(ResourceStatusEnum.UPLOADED)
                    .imageURL(imageUrl)
                    .build();
        }

        profileImageRepository.save(profileImage);

        return UpdateProfileImageDTO.builder()
                .id(profileImage.getId())
                .imageURL(profileImage.getImageURL())
                .status(profileImage.getStatus().name())
                .build();
    }
}
