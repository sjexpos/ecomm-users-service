package io.oigres.ecomm.service.users.usecases.users.admins.delete;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.repository.UserRepository;
import io.oigres.ecomm.service.users.repository.profiles.AdminProfileRepository;

import jakarta.transaction.Transactional;

@Component
@Transactional
public class DeleteAdminUserUseCaseImpl implements DeleteAdminUserUseCase {
    private final UserRepository userRepository;
    private final AdminProfileRepository adminProfileRepository;

    public DeleteAdminUserUseCaseImpl(UserRepository userRepository, AdminProfileRepository adminProfileRepository) {
        this.userRepository = userRepository;
        this.adminProfileRepository = adminProfileRepository;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public AdminProfile handle(Long userId) throws NotFoundProfileException, UserNotFoundException {
        AdminProfile profile = adminProfileRepository.findById(userId)
                .orElseThrow(NotFoundProfileException::new);
        Boolean isLastDeletedProfile = Boolean.FALSE;
        if (profile.getUser().getProfiles().stream()
                .filter( p -> !p.getId().equals(profile.getId()))
                .allMatch(p -> p.isDeleted())) {
            isLastDeletedProfile = Boolean.TRUE;
        }
        profile.setEnabled(Boolean.FALSE);
        profile.delete();
        adminProfileRepository.save(profile);

        if (isLastDeletedProfile) {
            profile.getUser().delete();
            userRepository.save(profile.getUser());
        }
        return profile;
    }
}