package io.oigres.ecomm.service.users.usecases.region.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.users.domain.State;

public interface GetStatesUseCase {

    Page<State> handle(Pageable pageable);
}
