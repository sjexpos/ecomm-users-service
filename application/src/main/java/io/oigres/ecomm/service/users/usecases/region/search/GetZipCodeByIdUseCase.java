package io.oigres.ecomm.service.users.usecases.region.search;

import io.oigres.ecomm.service.users.domain.ZipCode;
import io.oigres.ecomm.service.users.exception.ZipcodeNotFoundDomainException;

public interface GetZipCodeByIdUseCase {
    ZipCode handle(long zipCodeId) throws ZipcodeNotFoundDomainException;
}
