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

package io.oigres.ecomm.service.users.usecases.users.dispensaries.search;

import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class GetDispensaryByIdUseCaseImpl implements GetDispensaryByIdUseCase {
  private UserRepository userRepository;

  public GetDispensaryByIdUseCaseImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public DispensaryProfile handle(Long userId) throws NotFoundProfileException {
    User user = userRepository.findById(userId).orElseThrow(NotFoundProfileException::new);

    return (DispensaryProfile)
        user.getProfiles().stream()
            .filter(p -> ProfileTypeEnum.DISPENSARY.equals(p.getProfileType().getProfile()))
            .findFirst()
            .orElseThrow(NotFoundProfileException::new);
  }
}
