package io.oigres.ecomm.service.users.usecases.users.gender;

import io.oigres.ecomm.service.users.domain.Gender;
import io.oigres.ecomm.service.users.exception.NotFoundException;

public interface GetGenderByIdUseCase {
    Gender handle(long genderId) throws NotFoundException;
}
