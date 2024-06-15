package io.oigres.ecomm.service.users.usecases.users.dispensaries.create;

import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.exception.profile.DeletedProfileException;
import io.oigres.ecomm.service.users.exception.profile.ExistingProfileException;
import io.oigres.ecomm.service.users.exception.profile.TypeNotFoundProfileException;

public interface CreateNewDispensaryUserUseCase {
    DispensaryProfile handle(DispensaryProfile dispensaryProfile) throws DeletedProfileException, ExistingProfileException, TypeNotFoundProfileException;
}
