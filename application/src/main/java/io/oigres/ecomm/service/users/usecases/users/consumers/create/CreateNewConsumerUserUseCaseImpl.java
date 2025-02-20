/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package io.oigres.ecomm.service.users.usecases.users.consumers.create;

import io.oigres.ecomm.service.users.constants.ProfileErrorMessages;
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
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CreateNewConsumerUserUseCaseImpl implements CreateNewConsumerUserUseCase {
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

  public CreateNewConsumerUserUseCaseImpl(
      UserRepository userRepository,
      ProfileTypeRepository profileTypeRepository,
      GenderRepository genderRepository,
      StateRepository stateRepository,
      ZipCodeRepository zipCodeRepository,
      CardImageRepository cardImageRepository,
      ProfileImageRepository profileImageRepository,
      CardRepository cardRepository,
      ConsumerProfileRepository consumerProfileRepository,
      PasswordEncoder passwordEncoder) {
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
  }

  @Override
  @Transactional(Transactional.TxType.REQUIRED)
  public ConsumerProfile handle(
      String email,
      String password,
      String firstName,
      String lastName,
      String phone,
      String avatar,
      String cardImageURL,
      ConsumerTypeEnum userType,
      Long genderId,
      Long zipcodeStateId,
      Long zipcodeId)
      throws DeletedProfileException,
          ExistingProfileException,
          TypeNotFoundProfileException,
          GenderNotFoundException,
          ZipcodeNotFoundDomainException,
          StateNotFoundException,
          UserTypeNotFoundException {
    Optional<User> opUser = this.userRepository.findByEmail(email);

    User user;
    if (opUser.isPresent()) {
      user = opUser.orElseThrow(IllegalStateException::new);
      if (user.isDeleted()) {
        throw new DeletedProfileException(ProfileErrorMessages.PROFILE_DELETED);
      } else if (isConsumer(user)) {
        throw new ExistingProfileException(ProfileErrorMessages.PROFILE_ALREADY_EXIST);
      }
    } else {
      Set<Profile> profileSet = new HashSet<>();
      user =
          User.builder()
              .password(this.passwordEncoder.encode((password)))
              .email(email)
              .profiles(profileSet)
              .build();
    }
    ConsumerProfile consumerProfile =
        ConsumerProfile.builder().firstName(firstName).lastName(lastName).phone(phone).build();

    evalAndSetMmjCardImage(cardImageURL, consumerProfile);
    if (avatar != null) evalAndSetAvatarImage(avatar, consumerProfile);

    if (userType != null && userType.getId() != null) {
      consumerProfile.setUserType(
          ConsumerTypeEnum.getById(userType.getId())
              .orElseThrow(
                  () -> new UserTypeNotFoundException(ProfileErrorMessages.USER_TYPE_NOT_EXIST)));
    }
    ProfileType profileType =
        this.profileTypeRepository
            .findByProfile(ProfileTypeEnum.CONSUMER)
            .orElseThrow(
                () ->
                    new TypeNotFoundProfileException(ProfileErrorMessages.PROFILE_TYPE_NOT_FOUND));

    Gender gender =
        genderRepository
            .findById(genderId)
            .orElseThrow(() -> new GenderNotFoundException(ProfileErrorMessages.GENDER_NOT_EXIST));

    State state =
        this.stateRepository
            .findById(zipcodeStateId)
            .orElseThrow(() -> new StateNotFoundException(ProfileErrorMessages.STATE_NOT_FOUND));

    ZipCode zipCode =
        this.zipCodeRepository
            .findById(zipcodeId)
            .orElseThrow(
                () -> new ZipcodeNotFoundDomainException(ProfileErrorMessages.ZIPCODE_NOT_FOUND));
    zipCode.setState(state);

    consumerProfile.setZipCode(zipCode);
    consumerProfile.setGender(gender);
    consumerProfile.setVerified(Boolean.FALSE);
    consumerProfile.setProfileType(profileType);
    if (ResourceStatusEnum.UPLOADED.equals(consumerProfile.getCard().getCardImage().getStatus()))
      consumerProfile.setEnabled(Boolean.TRUE);
    else consumerProfile.setEnabled(Boolean.FALSE);
    consumerProfile.setUser(user);
    user.getProfiles().add(consumerProfile);
    this.userRepository.save(user);
    return consumerProfile;
  }

  private void evalAndSetAvatarImage(String avatarUrl, ConsumerProfile consumerProfile)
      throws ExistingProfileException {
    if (consumerProfileRepository.existsByProfileImage_ImageURL(avatarUrl))
      throw new ExistingProfileException(ProfileErrorMessages.PROFILE_ERROR_AVATAR_IMAGE_USED);
    ProfileImage image =
        profileImageRepository
            .findByImageURL(avatarUrl)
            .or(
                () ->
                    Optional.of(
                        ProfileImage.builder()
                            .imageURL(avatarUrl)
                            .status(ResourceStatusEnum.PENDING)
                            .build()))
            .orElse(null);
    consumerProfile.setProfileImage(image);
  }

  private void evalAndSetMmjCardImage(String cardImageURL, ConsumerProfile consumerProfile)
      throws ExistingProfileException {
    Optional<Card> cardOpt = cardRepository.findByCardImage_imageURL(cardImageURL);
    if (cardOpt.isPresent())
      throw new ExistingProfileException(ProfileErrorMessages.PROFILE_ERROR_MMJCARDIMAGE_USED);
    CardImage image =
        cardImageRepository
            .findByImageURL(cardImageURL)
            .or(() -> Optional.of(createCardImageUrlPending(cardImageURL)))
            .orElse(null);
    Card card = Card.builder().mmjCard(true).cardImage(image).idCard(true).build();
    consumerProfile.setCard(card);
  }

  private CardImage createCardImageUrlPending(String imageUrl) {
    return CardImage.builder().imageURL(imageUrl).status(ResourceStatusEnum.PENDING).build();
  }

  private Boolean isConsumer(User user) {
    return Profile.isAlreadyOfProfileType(user, ProfileTypeEnum.CONSUMER);
  }
}
