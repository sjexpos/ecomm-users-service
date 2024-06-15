package io.oigres.ecomm.service.users.usecases.users.dispensaries.update;

import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.exception.profile.ProfileUserException;

public interface UpdateDispensaryUseCase {
    public DispensaryProfile handle(Long userId, DispensaryProfile request) throws ProfileUserException;
}
