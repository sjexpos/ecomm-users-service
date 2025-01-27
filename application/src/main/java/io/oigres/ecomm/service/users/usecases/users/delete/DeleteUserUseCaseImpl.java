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

package io.oigres.ecomm.service.users.usecases.users.delete;

import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;
import io.oigres.ecomm.service.users.repository.UserRepository;
import io.oigres.ecomm.service.users.usecases.users.search.GetUserByIdUseCase;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
  private final GetUserByIdUseCase getUserByIdUseCase;
  private final UserRepository userRepository;

  public DeleteUserUseCaseImpl(
      GetUserByIdUseCase getUserByIdUseCase, UserRepository userRepository) {
    this.getUserByIdUseCase = getUserByIdUseCase;
    this.userRepository = userRepository;
  }

  public User handle(Long id) throws UserNotFoundException {
    User user = this.getUserByIdUseCase.handle(id);
    user.delete();
    user = this.userRepository.save(user);
    return user;
  }
}
