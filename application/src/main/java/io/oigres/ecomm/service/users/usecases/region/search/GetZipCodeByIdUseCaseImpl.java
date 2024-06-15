package io.oigres.ecomm.service.users.usecases.region.search;

import io.oigres.ecomm.service.users.constants.RegionServiceErrorMessages;
import io.oigres.ecomm.service.users.domain.ZipCode;
import io.oigres.ecomm.service.users.exception.ZipcodeNotFoundDomainException;
import io.oigres.ecomm.service.users.repository.ZipCodeRepository;

import org.springframework.stereotype.Component;

@Component
public class GetZipCodeByIdUseCaseImpl implements GetZipCodeByIdUseCase {
    private final ZipCodeRepository zipCodeRepository;

    public GetZipCodeByIdUseCaseImpl(ZipCodeRepository zipCodeRepository) {
        this.zipCodeRepository = zipCodeRepository;
    }

    @Override
    public ZipCode handle(long zipCodeId) throws ZipcodeNotFoundDomainException {
        return zipCodeRepository.findById(zipCodeId).orElseThrow(() -> new ZipcodeNotFoundDomainException(RegionServiceErrorMessages.ZIP_CODE_NOT_FOUND));
    }
}
