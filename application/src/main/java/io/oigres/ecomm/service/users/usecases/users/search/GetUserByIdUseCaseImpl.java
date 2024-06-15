package io.oigres.ecomm.service.users.usecases.users.search;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;
import io.oigres.ecomm.service.users.repository.UserRepository;

import java.util.Optional;

@Component
public class GetUserByIdUseCaseImpl implements GetUserByIdUseCase {
    private final UserRepository userRepository;

    public GetUserByIdUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handle(Long id) throws UserNotFoundException {
        Optional<User> opUser = this.userRepository.findById(id);
        return opUser.orElseThrow(UserNotFoundException::new);
    }
}
