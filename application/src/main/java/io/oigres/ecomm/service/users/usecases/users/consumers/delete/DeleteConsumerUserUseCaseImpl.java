package io.oigres.ecomm.service.users.usecases.users.consumers.delete;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.repository.UserRepository;
import io.oigres.ecomm.service.users.repository.profiles.ConsumerProfileRepository;

import jakarta.transaction.Transactional;

@Component
@Transactional
public class DeleteConsumerUserUseCaseImpl implements DeleteConsumerUserUseCase {
    private final UserRepository userRepository;
    private final ConsumerProfileRepository consumerProfileRepository;

    public DeleteConsumerUserUseCaseImpl(UserRepository userRepository, ConsumerProfileRepository consumerProfileRepository) {
        this.userRepository = userRepository;
        this.consumerProfileRepository = consumerProfileRepository;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public ConsumerProfile handle(Long userId) throws NotFoundProfileException, UserNotFoundException {
        ConsumerProfile profile = consumerProfileRepository.findById(userId)
                .orElseThrow(NotFoundProfileException::new);
        Boolean isLastDeletedProfile = Boolean.FALSE;
        if (profile.getUser().getProfiles().stream()
                .filter( p -> !p.getId().equals(profile.getId()))
                .allMatch(p -> p.isDeleted())) {
            isLastDeletedProfile = Boolean.TRUE;
        }
        profile.setEnabled(Boolean.FALSE);
        profile.delete();
        consumerProfileRepository.save(profile);

        if (isLastDeletedProfile) {
            profile.getUser().delete();
            userRepository.save(profile.getUser());
        }
        return profile;
    }
}