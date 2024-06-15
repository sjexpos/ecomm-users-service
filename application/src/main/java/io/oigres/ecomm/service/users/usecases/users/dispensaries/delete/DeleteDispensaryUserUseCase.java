package io.oigres.ecomm.service.users.usecases.users.dispensaries.delete;

import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;

public interface DeleteDispensaryUserUseCase {
    DispensaryProfile handle(Long dispensaryId) throws NotFoundProfileException, UserNotFoundException;
}
