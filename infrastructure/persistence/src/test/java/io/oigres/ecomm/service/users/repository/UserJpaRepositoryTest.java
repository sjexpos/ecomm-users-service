package io.oigres.ecomm.service.users.repository;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import io.oigres.ecomm.service.users.TestConfig;
import io.oigres.ecomm.service.users.domain.User;

@DataJpaTest(showSql = false)
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserJpaRepositoryTest {

    @Autowired TestEntityManager testEM;
    @Autowired UserJpaRepository userJpaRepository;

    @Test
    void test_find_by_email_and_password() {
        User user1 = User.builder()
            .email("user1@domain.com")
            .password("password1")
            .build();
        User user2 = User.builder()
            .email("user2@domain.com")
            .password("password2")
            .build();
        testEM.persist(user1);
        testEM.persist(user2);

        Optional<User> retrievedUser = this.userJpaRepository.findByEmailAndPassword("user1@domain.com", "password1");

        Assertions.assertNotNull(retrievedUser);
        Assertions.assertNotNull(retrievedUser.get().getId());
        Assertions.assertEquals("user1@domain.com", retrievedUser.get().getEmail());
        Assertions.assertEquals("password1", retrievedUser.get().getPassword());
        Assertions.assertEquals(TestConfig.AUDITOR_NAME, retrievedUser.get().getCreatedBy());
        Assertions.assertNotNull(retrievedUser.get().getCreatedAt());
        Assertions.assertEquals(TestConfig.AUDITOR_NAME, retrievedUser.get().getModifiedBy());
        Assertions.assertNotNull(retrievedUser.get().getModifiedAt());
        Assertions.assertNull(retrievedUser.get().getDeletedBy());
        Assertions.assertNull(retrievedUser.get().getDeletedAt());
    }

}
