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

package io.oigres.ecomm.service.users.usecases.users.profiles;

import io.oigres.ecomm.service.users.domain.Profile;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.exception.profile.*;
import io.oigres.ecomm.service.users.repository.profiles.ProfileRepository;
import io.oigres.ecomm.service.users.repository.profiles.ProfileTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class ToggleEnabledProfileUseCaseImpl implements ToggleEnabledProfileUseCase {

  private final ProfileRepository profileRepository;
  private final ProfileTypeRepository profileTypeRepository;

  public ToggleEnabledProfileUseCaseImpl(
      ProfileRepository profileRepository, ProfileTypeRepository profileTypeRepository) {
    this.profileRepository = profileRepository;
    this.profileTypeRepository = profileTypeRepository;
  }

  @Override
  public Profile handle(Long userId, ProfileTypeEnum profileType, Boolean enabledStatus)
      throws NotFoundProfileException, EnableStatusProfileException {
    Profile profile =
        profileRepository
            .findByIdAndProfileType(userId, profileType)
            .orElseThrow(NotFoundProfileException::new);
    if (enabledStatus.equals(profile.getEnabled())) {
      throw new EnableStatusProfileException(enabledStatus);
    }
    profile.setEnabled(enabledStatus);
    return profileRepository.save(profile);
  }
}
