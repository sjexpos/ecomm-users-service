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

package io.oigres.ecomm.service.users.usecases.users.dispensaries.update;

import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.exception.profile.ProfileUserException;
import io.oigres.ecomm.service.users.repository.profiles.DispensaryProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UpdateDispensaryUseCaseImpl implements UpdateDispensaryUseCase {
  private final DispensaryProfileRepository dispensaryProfileRepository;
  private final PasswordEncoder passwordEncoder;

  public UpdateDispensaryUseCaseImpl(
      DispensaryProfileRepository dispensaryProfileRepository, PasswordEncoder passwordEncoder) {
    this.dispensaryProfileRepository = dispensaryProfileRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional(Transactional.TxType.REQUIRED)
  public DispensaryProfile handle(Long userId, DispensaryProfile request)
      throws ProfileUserException {
    DispensaryProfile current =
        dispensaryProfileRepository.findById(userId).orElseThrow(NotFoundProfileException::new);
    if (request.getUser() != null
        && request.getUser().getPassword() != null
        && !passwordEncoder.matches(
            request.getUser().getPassword(), current.getUser().getPassword())) {
      current.getUser().setPassword(passwordEncoder.encode(request.getUser().getPassword()));
    }
    if (request.getEnabled() != null) {
      current.setEnabled(request.getEnabled());
    }
    updateDispensary(request, current);
    return dispensaryProfileRepository.save(current);
  }

  private void updateDispensary(DispensaryProfile request, DispensaryProfile current) {
    current.setDispensaryId(request.getDispensaryId());
  }
}
