package io.oigres.ecomm.service.users.usecases.users.admins.search;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.repository.UserRepository;

@Component
public class GetAdminByIdUseCaseImpl implements GetAdminByIdUseCase {

    private UserRepository userRepository;

    public GetAdminByIdUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AdminProfile handle(Long userId) throws NotFoundProfileException {
        User user = userRepository.findById(userId)
                .orElseThrow( NotFoundProfileException::new);

        return (AdminProfile) user.getProfiles().stream()
                .filter( p -> ProfileTypeEnum.ADMIN.equals(p.getProfileType().getProfile()) )
                .findFirst()
                .orElseThrow(NotFoundProfileException::new);
    }
}
