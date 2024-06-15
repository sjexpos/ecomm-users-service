package io.oigres.ecomm.service.users.usecases.users.admins.enable;

import io.oigres.ecomm.service.users.domain.Profile;
import io.oigres.ecomm.service.users.exception.profile.BadRequestProfileException;
import io.oigres.ecomm.service.users.exception.profile.EnableStatusProfileException;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.exception.profile.TypeNotFoundProfileException;

public interface EnableAdminUseCase {
    Profile handle(Long userId) throws NotFoundProfileException, TypeNotFoundProfileException, BadRequestProfileException, EnableStatusProfileException;
}
