package io.oigres.ecomm.service.users.usecases.users.sendcode;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.cache.VerificationCode;
import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.exception.InvalidException;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;
import io.oigres.ecomm.service.users.repository.cache.VerificationCodeRepository;
import io.oigres.ecomm.service.users.repository.profiles.ConsumerProfileRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Component
@Slf4j
public class SendCodeUseCaseImpl implements SendCodeUseCase {
    private final ConsumerProfileRepository consumerProfileRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final Random random = new Random();

    public SendCodeUseCaseImpl(ConsumerProfileRepository consumerProfileRepository, VerificationCodeRepository verificationCodeRepository) {
        this.consumerProfileRepository = consumerProfileRepository;
        this.verificationCodeRepository = verificationCodeRepository;
    }

    @Override
    public void handle(String email) throws UserNotFoundException, InvalidException {
        Optional<ConsumerProfile> opConsumerProfile = consumerProfileRepository.findByUserEmail(email);
        if (opConsumerProfile.isEmpty() || opConsumerProfile.get().getUser().isDeleted() || Boolean.FALSE.equals(opConsumerProfile.get().getEnabled())) {
            throw new UserNotFoundException();
        }
        ConsumerProfile consumerProfile = opConsumerProfile.get();
        if (Boolean.TRUE.equals(consumerProfile.getVerified())) {
            throw new InvalidException(String.format("User %s is already verified", email));
        }
        String code = String.format("%04d", random.nextInt(10000));
        verificationCodeRepository.save(VerificationCode.builder().email(email).code(code).createdAt(Instant.now()).build());
        String consumerPhone = consumerProfile.getPhone();
        sendSMS(consumerPhone, code);
    }

    private void sendSMS(String phone, String code) {
        log.info("TEMPORAL LOG - Sending to {} the verification code {}", phone, code);
    }
}
