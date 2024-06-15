package io.oigres.ecomm.service.users.usecases.users.profiles;

import io.oigres.ecomm.service.users.domain.Profile;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.exception.profile.BadRequestProfileException;
import io.oigres.ecomm.service.users.exception.profile.EnableStatusProfileException;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.exception.profile.TypeNotFoundProfileException;

public interface ToggleEnabledProfileUseCase {
    public Profile handle(Long userId, ProfileTypeEnum profileType, Boolean enabledStatus)
            throws NotFoundProfileException, EnableStatusProfileException, BadRequestProfileException;
}
