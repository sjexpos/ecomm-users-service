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

package io.oigres.ecomm.service.users.domain;

import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void check_get_and_set() {

    LocalDateTime created = LocalDateTime.now().minusMinutes(15);
    LocalDateTime updated = LocalDateTime.now().minusMinutes(5);
    AdminProfile profile = AdminProfile.builder().build();
    User user = User.builder().build();
    user.setId(14L);
    user.setCreatedAt(created);
    user.setCreatedBy("anyuser");
    user.setEmail("user@domain.com");
    user.setModifiedAt(updated);
    user.setModifiedBy("otheruser");
    user.setPassword("1234");
    user.setProfiles(Set.of(profile));

    Assertions.assertEquals(14L, user.getId());
    Assertions.assertEquals(created, user.getCreatedAt());
    Assertions.assertEquals("anyuser", user.getCreatedBy());
    Assertions.assertEquals("user@domain.com", user.getEmail());
    Assertions.assertEquals(updated, user.getModifiedAt());
    Assertions.assertEquals("otheruser", user.getModifiedBy());
    Assertions.assertEquals("1234", user.getPassword());
    Assertions.assertEquals(1, user.getProfiles().size());
    Assertions.assertEquals(profile, user.getProfiles().iterator().next());
  }
}
