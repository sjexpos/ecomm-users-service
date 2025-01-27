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

package io.oigres.ecomm.service.users.usecases.users.admins.update;

import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.repository.profiles.AdminProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UpdateAdminUseCaseImpl implements UpdateAdminUseCase {
  private final AdminProfileRepository adminProfileRepository;
  private final PasswordEncoder passwordEncoder;

  public UpdateAdminUseCaseImpl(
      AdminProfileRepository adminProfileRepository, PasswordEncoder passwordEncoder) {
    this.adminProfileRepository = adminProfileRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional(Transactional.TxType.REQUIRED)
  public AdminProfile handle(Long profileId, AdminProfile request) throws NotFoundProfileException {
    AdminProfile current =
        adminProfileRepository.findById(profileId).orElseThrow(NotFoundProfileException::new);
    if (request.getUser() != null
        && request.getUser().getPassword() != null
        && !passwordEncoder.matches(
            request.getUser().getPassword(), current.getUser().getPassword())) {
      current.getUser().setPassword(passwordEncoder.encode(request.getUser().getPassword()));
    }
    if (request.getUser().getEmail() != null) {
      current.getUser().setEmail(request.getUser().getEmail());
    }
    if (request.getEnabled() != null) {
      current.setEnabled(request.getEnabled());
    }
    updateAdmin(request, current);
    return adminProfileRepository.save(current);
  }

  private void updateAdmin(AdminProfile request, AdminProfile current) {
    current.setFirstName(request.getFirstName());
    current.setLastName(request.getLastName());
    current.setPhone(request.getPhone());
  }
}
