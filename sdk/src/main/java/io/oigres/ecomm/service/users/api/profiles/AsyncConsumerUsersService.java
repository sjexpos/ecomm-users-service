package io.oigres.ecomm.service.users.api.profiles;

import java.util.concurrent.Future;

import io.oigres.ecomm.service.users.api.model.consumer.GetConsumerStateTax;
import io.oigres.ecomm.service.users.api.model.consumer.GetConsumerUserResponse;
import io.oigres.ecomm.service.users.api.model.exception.NotFoundException;

public interface AsyncConsumerUsersService {
    Future<GetConsumerUserResponse> getConsumerUserByIdAsync(Long userId);

    Future<GetConsumerStateTax> getStateTaxByUserIdAsync(Long userId) throws NotFoundException;
}
