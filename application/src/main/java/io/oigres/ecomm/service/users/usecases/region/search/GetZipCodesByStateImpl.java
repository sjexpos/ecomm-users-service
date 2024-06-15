package io.oigres.ecomm.service.users.usecases.region.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.ZipCode;
import io.oigres.ecomm.service.users.repository.ZipCodeRepository;

@Component
public class GetZipCodesByStateImpl implements GetZipCodesByStateUseCase {
    private final ZipCodeRepository zipCodeRepository;

    public GetZipCodesByStateImpl(ZipCodeRepository zipCodeRepository) {
        this.zipCodeRepository = zipCodeRepository;
    }

    @Override
    public Page<ZipCode> handle(Long stateId, Pageable pageable) {
        return zipCodeRepository.findAllByStateId(stateId, pageable);
    }
}
