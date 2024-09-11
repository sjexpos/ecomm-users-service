package io.oigres.ecomm.service.users.usecases.users.dispensaries.delete;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.repository.UserRepository;
import io.oigres.ecomm.service.users.repository.profiles.DispensaryProfileRepository;

import jakarta.transaction.Transactional;

@Component
@Transactional
public class DeleteDispensaryUserUseCaseImpl implements DeleteDispensaryUserUseCase {
    private final UserRepository userRepository;
    private final DispensaryProfileRepository dispensaryProfileRepository;

    public DeleteDispensaryUserUseCaseImpl(UserRepository userRepository, DispensaryProfileRepository dispensaryProfileRepository) {
        this.userRepository = userRepository;
        this.dispensaryProfileRepository = dispensaryProfileRepository;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public DispensaryProfile handle(Long dispensaryId) throws NotFoundProfileException, UserNotFoundException {
        DispensaryProfile profile = dispensaryProfileRepository.findByDispensaryId(dispensaryId)
                .orElseThrow(NotFoundProfileException::new);
        Boolean isLastDeletedProfile = Boolean.FALSE;
        if (profile.getUser().getProfiles().stream()
                .filter( p -> !p.getId().equals(profile.getId()))
                .allMatch(p -> p.isDeleted())) {
            isLastDeletedProfile = Boolean.TRUE;
        }
        profile.setEnabled(Boolean.FALSE);
        profile.delete();
        dispensaryProfileRepository.save(profile);

        if (isLastDeletedProfile) {
            profile.getUser().delete();
            userRepository.save(profile.getUser());
        }
        return profile;
    }
}