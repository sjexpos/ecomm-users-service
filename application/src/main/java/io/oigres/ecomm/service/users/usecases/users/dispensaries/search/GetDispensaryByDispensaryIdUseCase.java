package io.oigres.ecomm.service.users.usecases.users.dispensaries.search;

import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;

public interface GetDispensaryByDispensaryIdUseCase {
    DispensaryProfile handle(Long dispensaryId) throws NotFoundProfileException;
}
