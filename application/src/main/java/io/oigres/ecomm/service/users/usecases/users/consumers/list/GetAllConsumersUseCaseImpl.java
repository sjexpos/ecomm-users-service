package io.oigres.ecomm.service.users.usecases.users.consumers.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.repository.profiles.ConsumerProfileRepository;

@Component
public class GetAllConsumersUseCaseImpl implements GetAllConsumersUseCase {
    private ConsumerProfileRepository consumerProfileRepository;

    public GetAllConsumersUseCaseImpl(ConsumerProfileRepository consumerProfileRepository) {
        this.consumerProfileRepository = consumerProfileRepository;
    }

    @Override
    public Page<ConsumerProfile> handle(Pageable pageable) {
        return consumerProfileRepository.findAll(pageable);
    }
}
