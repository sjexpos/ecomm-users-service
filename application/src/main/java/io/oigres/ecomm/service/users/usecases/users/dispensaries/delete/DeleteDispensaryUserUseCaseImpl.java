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

package io.oigres.ecomm.service.users.usecases.users.dispensaries.delete;

import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.repository.UserRepository;
import io.oigres.ecomm.service.users.repository.profiles.DispensaryProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class DeleteDispensaryUserUseCaseImpl implements DeleteDispensaryUserUseCase {
  private final UserRepository userRepository;
  private final DispensaryProfileRepository dispensaryProfileRepository;

  public DeleteDispensaryUserUseCaseImpl(
      UserRepository userRepository, DispensaryProfileRepository dispensaryProfileRepository) {
    this.userRepository = userRepository;
    this.dispensaryProfileRepository = dispensaryProfileRepository;
  }

  @Override
  @Transactional(Transactional.TxType.REQUIRED)
  public DispensaryProfile handle(Long dispensaryId)
      throws NotFoundProfileException, UserNotFoundException {
    DispensaryProfile profile =
        dispensaryProfileRepository
            .findByDispensaryId(dispensaryId)
            .orElseThrow(NotFoundProfileException::new);
    Boolean isLastDeletedProfile = Boolean.FALSE;
    if (profile.getUser().getProfiles().stream()
        .filter(p -> !p.getId().equals(profile.getId()))
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
