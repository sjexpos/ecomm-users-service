package io.oigres.ecomm.service.users.usecases.region.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.State;
import io.oigres.ecomm.service.users.repository.StateRepository;

@Component
public class GetStatesUseCaseImpl implements GetStatesUseCase {

    private final StateRepository stateRepository;

    public GetStatesUseCaseImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public Page<State> handle(Pageable pageable) {
        return stateRepository.findAll(pageable);
    }
}
