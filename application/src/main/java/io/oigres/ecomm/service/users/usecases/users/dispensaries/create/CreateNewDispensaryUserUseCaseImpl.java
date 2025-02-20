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

package io.oigres.ecomm.service.users.usecases.users.dispensaries.create;

import io.oigres.ecomm.service.users.constants.ProfileErrorMessages;
import io.oigres.ecomm.service.users.domain.Profile;
import io.oigres.ecomm.service.users.domain.ProfileType;
import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.exception.profile.DeletedProfileException;
import io.oigres.ecomm.service.users.exception.profile.ExistingProfileException;
import io.oigres.ecomm.service.users.exception.profile.TypeNotFoundProfileException;
import io.oigres.ecomm.service.users.repository.UserRepository;
import io.oigres.ecomm.service.users.repository.profiles.ProfileTypeRepository;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CreateNewDispensaryUserUseCaseImpl implements CreateNewDispensaryUserUseCase {
  private final UserRepository userRepository;
  private ProfileTypeRepository profileTypeRepository;
  private final PasswordEncoder passwordEncoder;

  public CreateNewDispensaryUserUseCaseImpl(
      UserRepository userRepository,
      ProfileTypeRepository profileTypeRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.profileTypeRepository = profileTypeRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional(Transactional.TxType.REQUIRED)
  public DispensaryProfile handle(DispensaryProfile dispensaryProfile)
      throws DeletedProfileException, ExistingProfileException, TypeNotFoundProfileException {
    Optional<User> opUser = this.userRepository.findByEmail(dispensaryProfile.getUser().getEmail());
    User user;
    if (opUser.isPresent()) {
      user = opUser.orElseThrow(IllegalStateException::new);
      if (user.isDeleted()) {
        throw new DeletedProfileException(ProfileErrorMessages.PROFILE_DELETED);
      } else if (isDispensary(user)) {
        throw new ExistingProfileException(ProfileErrorMessages.PROFILE_ALREADY_EXIST);
      }
    } else {
      Set<Profile> profileSet = new HashSet<>();
      user =
          User.builder()
              .password(this.passwordEncoder.encode(dispensaryProfile.getUser().getPassword()))
              .email(dispensaryProfile.getUser().getEmail())
              .profiles(profileSet)
              .build();
    }

    ProfileType profileType =
        this.profileTypeRepository
            .findByProfile(ProfileTypeEnum.DISPENSARY)
            .orElseThrow(
                () ->
                    new TypeNotFoundProfileException(ProfileErrorMessages.PROFILE_TYPE_NOT_FOUND));

    dispensaryProfile.setProfileType(profileType);
    dispensaryProfile.setEnabled(Boolean.TRUE);
    dispensaryProfile.setUser(user);
    user.getProfiles().add(dispensaryProfile);
    this.userRepository.save(user);
    return dispensaryProfile;
  }

  private Boolean isDispensary(User user) {
    return Profile.isAlreadyOfProfileType(user, ProfileTypeEnum.DISPENSARY);
  }
}
