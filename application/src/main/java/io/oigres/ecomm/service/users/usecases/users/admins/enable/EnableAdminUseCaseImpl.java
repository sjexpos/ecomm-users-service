package io.oigres.ecomm.service.users.usecases.users.admins.enable;

import io.oigres.ecomm.service.users.domain.Profile;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.exception.profile.BadRequestProfileException;
import io.oigres.ecomm.service.users.exception.profile.EnableStatusProfileException;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.exception.profile.TypeNotFoundProfileException;
import io.oigres.ecomm.service.users.usecases.users.profiles.ToggleEnabledProfileUseCase;

import org.springframework.stereotype.Component;

@Component
public class EnableAdminUseCaseImpl implements EnableAdminUseCase {

    private final ToggleEnabledProfileUseCase toggleEnabledProfileUseCase;

    public EnableAdminUseCaseImpl(ToggleEnabledProfileUseCase toggleEnabledProfileUseCase) {
        this.toggleEnabledProfileUseCase = toggleEnabledProfileUseCase;
    }

    @Override
    public Profile handle(Long userId) throws NotFoundProfileException, TypeNotFoundProfileException, BadRequestProfileException, EnableStatusProfileException {
        return toggleEnabledProfileUseCase.handle(userId, ProfileTypeEnum.ADMIN, Boolean.TRUE);
    }
}
