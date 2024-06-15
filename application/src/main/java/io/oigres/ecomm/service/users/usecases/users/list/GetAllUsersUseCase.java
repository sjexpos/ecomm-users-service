package io.oigres.ecomm.service.users.usecases.users.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.users.domain.User;

public interface GetAllUsersUseCase {
    
    Page<User> handle(Pageable pageable);

}
