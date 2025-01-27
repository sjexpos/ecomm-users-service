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
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(showSql = false)
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class UserSearchRepositoryTest {

  @Autowired TestEntityManager testEM;
  @Autowired UserJpaRepository userJpaRepository;

  @Test
  void should_index_user_data() throws Exception {
    User user1 = User.builder().email("user1@domain.com").password("password1").build();
    User user2 = User.builder().email("user2@domain.com").password("password2").build();
    userJpaRepository.saveAllAndFlush(List.of(user1, user2));

    int retry = 0;
    List<User> matchedUsers = List.of();
    while (retry < 3) {
      matchedUsers = userJpaRepository.searchBy("user1@domain.com", 10, "email");
      if (matchedUsers.isEmpty()) {
        Thread.sleep(500); // wait for Opensearch indexes the data
      } else {
        break;
      }
      retry++;
    }
    Assertions.assertNotNull(matchedUsers);
    Assertions.assertEquals(2, matchedUsers.size());
  }

  @AfterEach
  void tearDown() {
    List<User> users = userJpaRepository.findAll();
    users.stream().forEach(u -> userJpaRepository.delete(u));
  }
}
