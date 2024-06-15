package io.oigres.ecomm.service.users.usecases.users.validate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.exception.PasswordInvalidException;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;
import io.oigres.ecomm.service.users.repository.UserRepository;

import java.util.Optional;

@Component
public class ValidateUserUseCaseImpl implements ValidateUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ValidateUserUseCaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User handle(String email, String password) throws UserNotFoundException, PasswordInvalidException {
        Optional<User> opUser = this.userRepository.findByEmail(email);
        if (opUser.isEmpty() || opUser.get().isDeleted()) {
            throw new UserNotFoundException();
        }
        User user = opUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordInvalidException();
        }
        return user;
    }
}
