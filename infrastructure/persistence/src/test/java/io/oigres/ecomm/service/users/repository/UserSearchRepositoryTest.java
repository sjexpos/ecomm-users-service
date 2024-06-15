package io.oigres.ecomm.service.users.repository;

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

import io.oigres.ecomm.service.users.TestConfig;
import io.oigres.ecomm.service.users.domain.User;

@DataJpaTest(showSql = false)
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class UserSearchRepositoryTest {

    @Autowired TestEntityManager testEM;
    @Autowired UserJpaRepository userJpaRepository;

    @Test
    void should_index_user_data() throws Exception {
        User user1 = User.builder()
            .email("user1@domain.com")
            .password("password1")
            .build();
        User user2 = User.builder()
            .email("user2@domain.com")
            .password("password2")
            .build();
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
        users.stream()
            .forEach(u -> userJpaRepository.delete(u));
    }

}
