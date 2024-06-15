package io.oigres.ecomm.service.users.usecases.users.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.repository.UserRepository;

@Component
public class GetAllUsersUseCaseImpl implements GetAllUsersUseCase {
    private final UserRepository userRepository;

    public GetAllUsersUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> handle(Pageable pageable) {
        Page<User> page = this.userRepository.findAllByDeletedAtIsNull(pageable);
        return PageableExecutionUtils.getPage(page.getContent(), pageable, this.userRepository::countAllByDeletedAtIsNull);
    }
}
