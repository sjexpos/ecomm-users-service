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

package io.oigres.ecomm.service.users.repository;

import io.oigres.ecomm.service.users.TestConfig;
import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest(showSql = false)
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserJpaRepositoryTest {

  @Autowired TestEntityManager testEM;
  @Autowired UserJpaRepository userJpaRepository;

  @Test
  void test_find_by_email_and_password() throws UserNotFoundException {
    User user1 = User.builder().email("user1@domain.com").password("password1").build();
    User user2 = User.builder().email("user2@domain.com").password("password2").build();
    testEM.persist(user1);
    testEM.persist(user2);

    Optional<User> retrievedUser =
        this.userJpaRepository.findByEmailAndPassword("user1@domain.com", "password1");

    Assertions.assertNotNull(retrievedUser);
    User user = retrievedUser.orElseThrow(UserNotFoundException::new);
    Assertions.assertNotNull(user.getId());
    Assertions.assertEquals("user1@domain.com", user.getEmail());
    Assertions.assertEquals("password1", user.getPassword());
    Assertions.assertEquals(TestConfig.AUDITOR_NAME, user.getCreatedBy());
    Assertions.assertNotNull(user.getCreatedAt());
    Assertions.assertEquals(TestConfig.AUDITOR_NAME, user.getModifiedBy());
    Assertions.assertNotNull(user.getModifiedAt());
    Assertions.assertNull(user.getDeletedBy());
    Assertions.assertNull(user.getDeletedAt());
  }
}
