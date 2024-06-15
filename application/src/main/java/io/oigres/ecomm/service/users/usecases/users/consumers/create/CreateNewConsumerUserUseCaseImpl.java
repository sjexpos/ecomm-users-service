package io.oigres.ecomm.service.users.usecases.users.consumers.create;

import io.oigres.ecomm.service.users.api.ProfileErrorMessages;
import io.oigres.ecomm.service.users.api.model.consumer.CreateConsumerUserRequest;
import io.oigres.ecomm.service.users.domain.*;
import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.enums.ConsumerTypeEnum;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;
import io.oigres.ecomm.service.users.exception.GenderNotFoundException;
import io.oigres.ecomm.service.users.exception.StateNotFoundException;
import io.oigres.ecomm.service.users.exception.ZipcodeNotFoundDomainException;
import io.oigres.ecomm.service.users.exception.profile.*;
import io.oigres.ecomm.service.users.repository.*;
import io.oigres.ecomm.service.users.repository.profiles.CardImageRepository;
import io.oigres.ecomm.service.users.repository.profiles.CardRepository;
import io.oigres.ecomm.service.users.repository.profiles.ConsumerProfileRepository;
import io.oigres.ecomm.service.users.repository.profiles.ProfileTypeRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class CreateNewConsumerUserUseCaseImpl implements CreateNewConsumerUserUseCase{
    private final UserRepository userRepository;
    private final ProfileTypeRepository profileTypeRepository;
    private final GenderRepository genderRepository;
    private final StateRepository stateRepository;
    private final ZipCodeRepository zipCodeRepository;
    private final CardImageRepository cardImageRepository;
    private final ProfileImageRepository profileImageRepository;
    private final CardRepository cardRepository;
    private final ConsumerProfileRepository consumerProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    public CreateNewConsumerUserUseCaseImpl(UserRepository userRepository, ProfileTypeRepository profileTypeRepository, GenderRepository genderRepository, StateRepository stateRepository, ZipCodeRepository zipCodeRepository, CardImageRepository cardImageRepository, ProfileImageRepository profileImageRepository, CardRepository cardRepository, ConsumerProfileRepository consumerProfileRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.profileTypeRepository = profileTypeRepository;
        this.genderRepository = genderRepository;
        this.stateRepository = stateRepository;
        this.zipCodeRepository = zipCodeRepository;
        this.cardImageRepository = cardImageRepository;
        this.profileImageRepository = profileImageRepository;
        this.cardRepository = cardRepository;
        this.consumerProfileRepository = consumerProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }


    @Override
    public ConsumerProfile handle(CreateConsumerUserRequest consumerProfileReq) throws DeletedProfileException, ExistingProfileException, TypeNotFoundProfileException, GenderNotFoundException, ZipcodeNotFoundDomainException, StateNotFoundException, UserTypeNotFoundException {
        Optional<User> opUser = this.userRepository.findByEmail(consumerProfileReq.getEmail());

        User user;
        if (opUser.isPresent() ) {
            user = opUser.get();
            if(user.isDeleted()) {
                throw new DeletedProfileException(ProfileErrorMessages.PROFILE_DELETED);
            } else if (isConsumer(user)) {
                throw new ExistingProfileException(ProfileErrorMessages.PROFILE_ALREADY_EXIST);
            }
        }
        else {
            Set<Profile> profileSet = new HashSet<>();
            user = User.builder()
                    .password( this.passwordEncoder.encode((consumerProfileReq.getPassword())) )
                    .email(consumerProfileReq.getEmail())
                    .profiles(profileSet)
                    .build();
        }
        ConsumerProfile consumerProfile = modelMapper.map(consumerProfileReq, ConsumerProfile.class);

        evalAndSetMmjCardImage(consumerProfileReq.getCardImageURL(),consumerProfile);
        if (consumerProfileReq.getAvatar() != null)
            evalAndSetAvatarImage(consumerProfileReq.getAvatar(),consumerProfile);

        if (consumerProfileReq.getUserType() != null && consumerProfileReq.getUserType().getId() != null) {
            consumerProfile.setUserType(ConsumerTypeEnum.getById(consumerProfileReq.getUserType().getId())
                    .orElseThrow(() -> new UserTypeNotFoundException(ProfileErrorMessages.USER_TYPE_NOT_EXIST)));
        }
        ProfileType profileType = this.profileTypeRepository.findByProfile(ProfileTypeEnum.CONSUMER)
                .orElseThrow(() -> new TypeNotFoundProfileException(ProfileErrorMessages.PROFILE_TYPE_NOT_FOUND));

        Gender gender = genderRepository.findById(consumerProfileReq.getGenderId())
                .orElseThrow(() -> new GenderNotFoundException(ProfileErrorMessages.GENDER_NOT_EXIST));

        State state = this.stateRepository.findById(consumerProfileReq.getZipcodeStateId())
                .orElseThrow(() -> new StateNotFoundException(ProfileErrorMessages.STATE_NOT_FOUND));

        ZipCode zipCode = this.zipCodeRepository.findById(consumerProfileReq.getZipcodeId())
                .orElseThrow(() -> new ZipcodeNotFoundDomainException(ProfileErrorMessages.ZIPCODE_NOT_FOUND));
        zipCode.setState(state);


        consumerProfile.setZipCode(zipCode);
        consumerProfile.setGender(gender);
        consumerProfile.setVerified(Boolean.FALSE);
        consumerProfile.setProfileType(profileType);
        if (ResourceStatusEnum.UPLOADED.equals(consumerProfile.getCard().getCardImage().getStatus()))
            consumerProfile.setEnabled(Boolean.TRUE);
        else
            consumerProfile.setEnabled(Boolean.FALSE);
        consumerProfile.setUser(user);
        user.getProfiles().add(consumerProfile);
        this.userRepository.save(user);
        return consumerProfile;
    }

    private void evalAndSetAvatarImage(String avatarUrl, ConsumerProfile consumerProfile) throws ExistingProfileException {
        if (consumerProfileRepository.existsByProfileImage_ImageURL(avatarUrl))
            throw new ExistingProfileException(ProfileErrorMessages.PROFILE_ERROR_AVATAR_IMAGE_USED);
        ProfileImage image = profileImageRepository.findByImageURL(avatarUrl).or(() ->
                        Optional.of(ProfileImage.builder().imageURL(avatarUrl).status(ResourceStatusEnum.PENDING).build()))
                .get();
        consumerProfile.setProfileImage(image);
    }

    private void evalAndSetMmjCardImage(String cardImageURL, ConsumerProfile consumerProfile) throws ExistingProfileException {
        Optional<Card> cardOpt = cardRepository.findByCardImage_imageURL(cardImageURL);
        if (cardOpt.isPresent())
            throw new ExistingProfileException(ProfileErrorMessages.PROFILE_ERROR_MMJCARDIMAGE_USED);
        CardImage image = cardImageRepository.findByImageURL(cardImageURL).or(() ->
                    Optional.of(createCardImageUrlPending(cardImageURL)))
                    .get();
        Card card = Card.builder().mmjCard(true).cardImage(image).idCard(true).build();
        consumerProfile.setCard(card);
    }

    private CardImage createCardImageUrlPending(String imageUrl) {
        return CardImage.builder()
                .imageURL(imageUrl)
                .status(ResourceStatusEnum.PENDING)
                .build();
    }

    private Boolean isConsumer(User user) {
        return Profile.isAlreadyOfProfileType(user, ProfileTypeEnum.CONSUMER);
    }
}
