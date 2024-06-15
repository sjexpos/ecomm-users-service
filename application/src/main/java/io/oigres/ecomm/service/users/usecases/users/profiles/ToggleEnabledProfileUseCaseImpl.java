package io.oigres.ecomm.service.users.usecases.users.profiles;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.Profile;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.exception.profile.*;
import io.oigres.ecomm.service.users.repository.profiles.ProfileRepository;
import io.oigres.ecomm.service.users.repository.profiles.ProfileTypeRepository;

@Component
public class ToggleEnabledProfileUseCaseImpl implements ToggleEnabledProfileUseCase {

    private final ProfileRepository profileRepository;
    private final ProfileTypeRepository profileTypeRepository;

    public ToggleEnabledProfileUseCaseImpl(ProfileRepository profileRepository, ProfileTypeRepository profileTypeRepository) {
        this.profileRepository = profileRepository;
        this.profileTypeRepository = profileTypeRepository;
    }

    @Override
    public Profile handle(Long userId, ProfileTypeEnum profileType, Boolean enabledStatus)
            throws NotFoundProfileException, EnableStatusProfileException {
        Profile profile = profileRepository.findByIdAndProfileType(userId, profileType)
                .orElseThrow(NotFoundProfileException::new);
        if (enabledStatus.equals(profile.getEnabled())) {
            throw new EnableStatusProfileException(enabledStatus);
        }
        profile.setEnabled(enabledStatus);
        return profileRepository.save(profile);
    }
}
