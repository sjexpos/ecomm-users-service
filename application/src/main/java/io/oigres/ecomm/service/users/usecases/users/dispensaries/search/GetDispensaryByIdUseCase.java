package io.oigres.ecomm.service.users.usecases.users.dispensaries.search;

import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;

public interface GetDispensaryByIdUseCase {
    DispensaryProfile handle(Long userId) throws NotFoundProfileException;
}
