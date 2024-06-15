package io.oigres.ecomm.service.users.usecases.users.delete;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;
import io.oigres.ecomm.service.users.repository.UserRepository;
import io.oigres.ecomm.service.users.usecases.users.search.GetUserByIdUseCase;

@Component
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UserRepository userRepository;
    
    public DeleteUserUseCaseImpl(GetUserByIdUseCase getUserByIdUseCase, UserRepository userRepository) {
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.userRepository = userRepository;
    }

    public User handle(Long id) throws UserNotFoundException {
        User user = this.getUserByIdUseCase.handle(id);
        user.delete();
        user = this.userRepository.save(user);
        return user;
    }

}
