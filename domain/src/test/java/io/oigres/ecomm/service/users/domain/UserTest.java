package io.oigres.ecomm.service.users.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.oigres.ecomm.service.users.domain.profile.AdminProfile;

import java.time.LocalDateTime;
import java.util.Set;

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
