package io.oigres.ecomm.service.users.usecases.region.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.users.domain.ZipCode;

public interface GetZipCodesByStateUseCase {

    Page<ZipCode> handle(Long stateId, Pageable pageable);
}
