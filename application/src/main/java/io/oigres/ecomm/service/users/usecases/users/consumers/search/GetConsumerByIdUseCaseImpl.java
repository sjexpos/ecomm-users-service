package io.oigres.ecomm.service.users.usecases.users.consumers.search;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.repository.UserRepository;

@Component
public class GetConsumerByIdUseCaseImpl implements GetConsumerByIdUseCase {
    private UserRepository userRepository;

    public GetConsumerByIdUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ConsumerProfile handle(Long userId) throws NotFoundProfileException {
        User user = userRepository.findById(userId)
                .orElseThrow( NotFoundProfileException::new);

        return (ConsumerProfile) user.getProfiles().stream()
                .filter( p -> ProfileTypeEnum.CONSUMER.equals(p.getProfileType().getProfile()))
                .findAny()
                .orElseThrow( NotFoundProfileException::new);
    }
}
