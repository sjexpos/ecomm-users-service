package io.oigres.ecomm.service.users.usecases.users.verifycode;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.cache.VerificationCode;
import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.exception.InvalidException;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;
import io.oigres.ecomm.service.users.repository.cache.VerificationCodeRepository;
import io.oigres.ecomm.service.users.repository.profiles.ConsumerProfileRepository;

import java.util.Optional;

@Component
public class VerifyCodeUseCaseImpl implements VerifyCodeUseCase {
    private final ConsumerProfileRepository consumerProfileRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    public VerifyCodeUseCaseImpl(ConsumerProfileRepository consumerProfileRepository, VerificationCodeRepository verificationCodeRepository) {
        this.consumerProfileRepository = consumerProfileRepository;
        this.verificationCodeRepository = verificationCodeRepository;
    }

    @Override
    public void handle(String email, String code) throws UserNotFoundException, InvalidException {
        Optional<ConsumerProfile> opConsumerProfile = consumerProfileRepository.findByUserEmail(email);
        if (opConsumerProfile.isEmpty() || opConsumerProfile.get().getUser().isDeleted() || Boolean.FALSE.equals(opConsumerProfile.get().getEnabled())) {
            throw new UserNotFoundException();
        }
        ConsumerProfile consumerProfile = opConsumerProfile.get();
        if (Boolean.TRUE.equals(consumerProfile.getVerified())) {
            throw new InvalidException(String.format("User %s is already verified", email));
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);
        if (verificationCode == null || !verificationCode.getCode().equals(code)) {
            throw new InvalidException(String.format("Invalid code %s", code));
        }
        consumerProfile.setVerified(true);
        consumerProfileRepository.save(consumerProfile);
    }
}
