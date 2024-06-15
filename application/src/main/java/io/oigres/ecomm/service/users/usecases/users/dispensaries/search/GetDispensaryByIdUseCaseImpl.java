package io.oigres.ecomm.service.users.usecases.users.dispensaries.search;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.repository.UserRepository;

@Component
public class GetDispensaryByIdUseCaseImpl implements GetDispensaryByIdUseCase {
    private UserRepository userRepository;

    public GetDispensaryByIdUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public DispensaryProfile handle(Long userId) throws NotFoundProfileException {
        User user = userRepository.findById(userId)
                .orElseThrow( NotFoundProfileException::new);

        return (DispensaryProfile) user.getProfiles().stream()
                .filter( p -> ProfileTypeEnum.DISPENSARY.equals(p.getProfileType().getProfile()))
                .findFirst()
                .orElseThrow( NotFoundProfileException::new);
    }
}
