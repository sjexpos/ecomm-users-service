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

package io.oigres.ecomm.service.users.usecases.users.admins.delete;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import io.oigres.ecomm.service.users.domain.ProfileType;
import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.repository.UserRepository;
import io.oigres.ecomm.service.users.repository.profiles.AdminProfileRepository;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class DeleteAdminUserUseCaseTest {

  @TestConfiguration
  @ComponentScan
  static class TestConfig {}

  @Autowired DeleteAdminUserUseCase useCase;
  @MockBean UserRepository userRepository;
  @MockBean AdminProfileRepository adminProfileRepository;

  @Test
  void should_delete_admin_user() throws Exception {
    // org.mockito.BDDMockito.willDoNothing().given(userRepository).deleteById(eq(15L));

    String email = "user@domain.com";
    AdminProfile adminProfile =
        AdminProfile.builder()
            .id(10L)
            .firstName("john")
            .lastName("doe")
            .user(new User())
            .profileType(ProfileType.builder().profile(ProfileTypeEnum.ADMIN).build())
            .build();
    adminProfile.getUser().setId(15L);
    adminProfile.getUser().setEmail(email);
    adminProfile.getUser().setProfiles(Set.of(adminProfile));
    given(this.adminProfileRepository.findById(15L)).willReturn(Optional.of(adminProfile));

    AdminProfile profile = useCase.handle(15L);

    Assertions.assertNotNull(profile);
    ArgumentCaptor<AdminProfile> profileSaveCaptor = ArgumentCaptor.forClass(AdminProfile.class);
    then(this.adminProfileRepository).should().save(profileSaveCaptor.capture());
    Assertions.assertEquals(Boolean.FALSE, profileSaveCaptor.getValue().getEnabled());
    Assertions.assertNotNull(profileSaveCaptor.getValue().getDeletedAt());

    ArgumentCaptor<User> userSaveCaptor = ArgumentCaptor.forClass(User.class);
    then(this.userRepository).should().save(userSaveCaptor.capture());
    Assertions.assertNotNull(userSaveCaptor.getValue().getDeletedAt());
  }
}
