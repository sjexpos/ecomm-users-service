package io.oigres.ecomm.service.users.usecases.users.consumers.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;

public interface GetAllConsumersUseCase {
    Page<ConsumerProfile> handle(Pageable pageable);
}
