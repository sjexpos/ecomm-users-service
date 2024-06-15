package io.oigres.ecomm.service.users.usecases.users.admins.create;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.oigres.ecomm.service.users.domain.Profile;
import io.oigres.ecomm.service.users.domain.ProfileType;
import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.exception.profile.DeletedProfileException;
import io.oigres.ecomm.service.users.exception.profile.ExistingProfileException;
import io.oigres.ecomm.service.users.repository.UserRepository;
import io.oigres.ecomm.service.users.repository.profiles.ProfileTypeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(SpringExtension.class)
class CreateNewAdminUserUseCaseTest {

    @TestConfiguration @ComponentScan static class TestConfig {}

    @Autowired CreateNewAdminUserUseCase useCase;
    @MockBean UserRepository userRepository;
    @MockBean ProfileTypeRepository profileTypeRepository;
    @MockBean PasswordEncoder passwordEncoder;

    @Test
    void should_throw_DeletedProfileException() {
        String email = "user@domain.com";
        User user = User.builder()
                .id(15L)
                .email(email)
                .deletedAt(LocalDateTime.now().minusHours(1))
                .deletedBy("anyuser")
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        AdminProfile adminProfile = AdminProfile.builder()
                .firstName("john")
                .lastName("doe")
                .user(new User())
                .build();
        adminProfile.getUser().setEmail(email);

        Assertions.assertThrowsExactly(DeletedProfileException.class, () -> useCase.handle(adminProfile));
    }

    @Test
    void should_throw_ExistingProfileException() {
        String email = "user@domain.com";
        Profile profile = AdminProfile.builder().profileType(ProfileType.builder().profile(ProfileTypeEnum.ADMIN).build()).build();
        User user = User.builder()
                .id(15L)
                .email(email)
                .profiles(Set.of(profile))
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        AdminProfile adminProfile = AdminProfile.builder()
                .id(10L)
                .firstName("john")
                .lastName("doe")
                .user(new User())
                .profileType(ProfileType.builder().profile(ProfileTypeEnum.ADMIN).build())
                .build();
        adminProfile.getUser().setEmail(email);
        adminProfile.getUser().setProfiles(Set.of(adminProfile));

        Assertions.assertThrowsExactly(ExistingProfileException.class, () -> useCase.handle(adminProfile));
    }

    @Test
    void should_create_an_admin_profile() throws Exception {
        String email = "user@domain.com";
        String password = "password";
        String encodedPassword = "encoded_password";
        given(userRepository.findByEmail(email)).willReturn(Optional.ofNullable(null));
        ProfileType profileType = ProfileType.builder().build();
        given(this.passwordEncoder.encode(password)).willReturn(encodedPassword);
        given(this.profileTypeRepository.findByProfile(ProfileTypeEnum.ADMIN)).willReturn(Optional.of(profileType));

        AdminProfile adminProfile = AdminProfile.builder()
            .firstName("john")
            .lastName("doe")
            .user(new User())
            .profileType(ProfileType.builder().profile(ProfileTypeEnum.ADMIN).build())
            .build();
        adminProfile.getUser().setEmail(email);
        adminProfile.getUser().setProfiles(Set.of(adminProfile));
        adminProfile.getUser().setPassword(password);

        AdminProfile createdProfile = useCase.handle(adminProfile);

        Assertions.assertNotNull(createdProfile);
        then(this.userRepository).should(times(1)).save(any(User.class));
    }

}
