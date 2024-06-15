package io.oigres.ecomm.service.users.usecases.users.consumers.update;

import io.oigres.ecomm.service.users.api.ProfileErrorMessages;
import io.oigres.ecomm.service.users.domain.*;
import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.enums.ConsumerTypeEnum;
import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;
import io.oigres.ecomm.service.users.exception.GenderNotFoundException;
import io.oigres.ecomm.service.users.exception.StateNotFoundException;
import io.oigres.ecomm.service.users.exception.ZipcodeNotFoundDomainException;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.exception.profile.ProfileUserException;
import io.oigres.ecomm.service.users.repository.GenderRepository;
import io.oigres.ecomm.service.users.repository.ProfileImageRepository;
import io.oigres.ecomm.service.users.repository.StateRepository;
import io.oigres.ecomm.service.users.repository.ZipCodeRepository;
import io.oigres.ecomm.service.users.repository.profiles.CardImageRepository;
import io.oigres.ecomm.service.users.repository.profiles.ConsumerProfileRepository;
import io.oigres.ecomm.service.users.repository.profiles.ProfileRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

@Component
public class UpdateConsumerUseCaseImpl implements UpdateConsumerUseCase{
    private final ProfileRepository profileRepository;
    private final ConsumerProfileRepository consumerProfileRepository;
    private final GenderRepository genderRepository;
    private final StateRepository stateRepository;
    private final ZipCodeRepository zipCodeRepository;
    private final ProfileImageRepository profileImageRepository;
    private final CardImageRepository cardImageRepository;
    private final PasswordEncoder passwordEncoder;

    public UpdateConsumerUseCaseImpl(ProfileRepository profileRepository, ConsumerProfileRepository consumerProfileRepository, GenderRepository genderRepository, StateRepository stateRepository, ZipCodeRepository zipCodeRepository, ProfileImageRepository profileImageRepository, CardImageRepository cardImageRepository, PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.consumerProfileRepository = consumerProfileRepository;
        this.genderRepository = genderRepository;
        this.stateRepository = stateRepository;
        this.zipCodeRepository = zipCodeRepository;
        this.profileImageRepository = profileImageRepository;
        this.cardImageRepository = cardImageRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public ConsumerProfile handle(Long userId, ConsumerProfile request) throws ProfileUserException, GenderNotFoundException, StateNotFoundException, ZipcodeNotFoundDomainException {
        ConsumerProfile current = consumerProfileRepository.findById(userId)
                .orElseThrow(NotFoundProfileException::new);

        if (request.getProfileImage() == null) {
            request.setProfileImage(new ProfileImage(null, null, ResourceStatusEnum.PENDING, LocalDateTime.now()));            
        } 
        if (request.getProfileImage().getImageURL() != null && !StringUtils.hasText(request.getProfileImage().getImageURL())) {
            throw new ProfileUserException(ProfileErrorMessages.INVALID_PROFILE_IMAGE);
        }

        if(request.getUser() != null && request.getUser().getPassword() != null &&
                !passwordEncoder.matches(request.getUser().getPassword(), current.getUser().getPassword())) {
            current.getUser().setPassword(passwordEncoder.encode(request.getUser().getPassword()));
        }

        if (request.getUserType().getId()!=null) {
            current.setUserType(ConsumerTypeEnum.getById(request.getUserType().getId())
                    .orElseThrow(NotFoundProfileException::new));
        }

        if (request.getEnabled() != null) {
            current.setEnabled(request.getEnabled());
        }

        Gender gender = genderRepository.findById(request.getGender().getId())
                .orElseThrow(() -> new GenderNotFoundException(ProfileErrorMessages.GENDER_NOT_EXIST));

        State state = this.stateRepository.findById(request.getZipCode().getState().getId())
                .orElseThrow(() -> new StateNotFoundException(ProfileErrorMessages.STATE_NOT_FOUND));

        ZipCode zipCode = this.zipCodeRepository.findById(request.getZipCode().getId())
                .orElseThrow(() -> new ZipcodeNotFoundDomainException(ProfileErrorMessages.ZIPCODE_NOT_FOUND));

        request.setGender(gender);
        zipCode.setState(state);
        request.setZipCode(zipCode);

        updateConsumer(request, current);
        updateProfileImage(current, request);
        if (request.getCard().getCardImage() != null)
            updateCardImage(current,request);

        return consumerProfileRepository.save(current);
    }

    private void updateCardImage(ConsumerProfile current, ConsumerProfile request) {
        CardImage currentCardImage = current.getCard().getCardImage();

        if(currentCardImage != null
                && !request.getCard().getCardImage().getImageURL().equals(currentCardImage.getImageURL())) {
            currentCardImage.setStatus(ResourceStatusEnum.DELETED);
            cardImageRepository.save(currentCardImage);
            CardImage image = cardImageRepository.findByImageURL(request.getCard().getCardImage().getImageURL())
                    .or(() -> Optional.of(CardImage.builder()
                            .imageURL(request.getCard().getCardImage().getImageURL())
                            .status(ResourceStatusEnum.PENDING)
                            .build()))
                    .get();
            cardImageRepository.save(image);
            current.getCard().setCardImage(image);
        }
    }

    private void updateConsumer(ConsumerProfile request, ConsumerProfile current) {
        current.setFirstName(request.getFirstName());
        current.setLastName(request.getLastName());
        current.getCard().setMmjCard(request.getCard().getMmjCard());
        current.getCard().setIdCard(request.getCard().getIdCard());
        current.setGender(request.getGender());
        current.setPhone(request.getPhone());
        current.setZipCode(request.getZipCode());
    }

    private void updateProfileImage(ConsumerProfile current, ConsumerProfile request) throws ProfileUserException {
        ProfileImage currentProfileImage = current.getProfileImage();
        ProfileImage newProfileImage = request.getProfileImage();
        if(currentProfileImage != null && !currentProfileImage.getImageURL().equals(newProfileImage.getImageURL())) {
            currentProfileImage.setStatus(ResourceStatusEnum.DELETED);
            current.setProfileImage(null);
            profileImageRepository.save(currentProfileImage);
        }
        if (newProfileImage.getImageURL() != null && (currentProfileImage == null || !newProfileImage.getImageURL().equals(currentProfileImage.getImageURL()))) {
            List<Profile> profiles = this.profileRepository.existsByNotIdAndProfileImage_ImageURL(current.getId(), newProfileImage.getImageURL());
            if (!profiles.isEmpty()) {
                throw new ProfileUserException(ProfileErrorMessages.IMAGE_ALREADY_EXISTS);
            }
            profileImageRepository.save(newProfileImage);
            current.setProfileImage(newProfileImage);
        }
    }

}
