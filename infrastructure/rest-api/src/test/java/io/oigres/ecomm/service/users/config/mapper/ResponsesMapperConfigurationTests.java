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

package io.oigres.ecomm.service.users.config.mapper;

import io.oigres.ecomm.service.users.api.model.GetAllUsersResponse;
import io.oigres.ecomm.service.users.api.model.admin.CreateAdminUserRequest;
import io.oigres.ecomm.service.users.api.model.admin.CreateAdminUserResponse;
import io.oigres.ecomm.service.users.api.model.admin.GetAdminUserResponse;
import io.oigres.ecomm.service.users.api.model.consumer.CreateConsumerUserResponse;
import io.oigres.ecomm.service.users.api.model.consumer.GetConsumerUserResponse;
import io.oigres.ecomm.service.users.api.model.dispensary.CreateDispensaryUserRequest;
import io.oigres.ecomm.service.users.api.model.dispensary.CreateDispensaryUserResponse;
import io.oigres.ecomm.service.users.api.model.dispensary.GetDispensaryUserResponse;
import io.oigres.ecomm.service.users.api.model.enums.ConsumerTypeApiEnum;
import io.oigres.ecomm.service.users.api.model.region.StateResponse;
import io.oigres.ecomm.service.users.api.txoutbox.UserOutbox;
import io.oigres.ecomm.service.users.domain.*;
import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.enums.ConsumerTypeEnum;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ResponsesMapperConfigurationTests {
  @TestConfiguration
  @ComponentScan
  static class TestConfig {}

  @Autowired ResponsesMapper responsesMapper;

  @Test
  void testUser2GetAllUsersResponse() {
    User user1 =
        User.builder()
            .id(10L)
            .email("test@yopmail.com")
            .password("1234")
            .createdAt(LocalDateTime.now())
            .createdBy("somebody1")
            .modifiedAt(LocalDateTime.now())
            .modifiedBy("somebody2")
            .build();
    User user2 =
        User.builder()
            .id(12L)
            .email("test2@yopmail.com")
            .password("123456")
            .createdAt(LocalDateTime.now())
            .createdBy("somebody3")
            .modifiedAt(LocalDateTime.now())
            .modifiedBy("somebody4")
            .build();
    GetAllUsersResponse out = this.responsesMapper.toGetAllUsersResponse(user1);
    Assertions.assertNotNull(out);
    Assertions.assertEquals(user1.getId(), out.getUserId());
    Assertions.assertEquals(user1.getEmail(), out.getEmail());

    List<GetAllUsersResponse> list =
        this.responsesMapper.toGetAllUsersResponse(List.of(user1, user2));
    Assertions.assertNotNull(list);
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(user1.getId(), list.get(0).getUserId());
    Assertions.assertEquals(user1.getEmail(), list.get(0).getEmail());
    Assertions.assertEquals(user2.getId(), list.get(1).getUserId());
    Assertions.assertEquals(user2.getEmail(), list.get(1).getEmail());
  }

  @Test
  void testCreateAdminUserRequest2AdminProfile() {
    CreateAdminUserRequest request =
        CreateAdminUserRequest.builder()
            .email("test@yopmail.com")
            .firstName("John")
            .lastName("Dou")
            .phone("5491123452357")
            .password("1234")
            .avatar("http://photos/24312345.png")
            .build();
    AdminProfile out = this.responsesMapper.toAdminProfile(request);
    Assertions.assertNotNull(out);
    Assertions.assertEquals(request.getEmail(), out.getUser().getEmail());
    Assertions.assertEquals(request.getFirstName(), out.getFirstName());
    Assertions.assertEquals(request.getLastName(), out.getLastName());
    Assertions.assertEquals(request.getPhone(), out.getPhone());
    Assertions.assertEquals(request.getPassword(), out.getUser().getPassword());
    Assertions.assertEquals(request.getAvatar(), out.getProfileImage().getImageURL());
  }

  @Test
  void testAdminProfile2CreateAdminUserResponse() {
    User user =
        User.builder()
            .id(10L)
            .email("test@yopmail.com")
            .password("1234")
            .createdAt(LocalDateTime.now())
            .createdBy("somebody1")
            .modifiedAt(LocalDateTime.now())
            .modifiedBy("somebody2")
            .build();
    ProfileType profileType = ProfileType.builder().id(2L).profile(ProfileTypeEnum.ADMIN).build();
    AdminProfile profile =
        AdminProfile.builder()
            .id(15L)
            .firstName("John")
            .lastName("Dou")
            .phone("549113457432")
            .enabled(true)
            .profileType(profileType)
            .profileImage(ProfileImage.builder().id(3L).build())
            .createdAt(LocalDateTime.now())
            .createdBy("somebody")
            .modifiedAt(LocalDateTime.now())
            .modifiedBy("somebody2")
            .user(user)
            .build();

    CreateAdminUserResponse out = this.responsesMapper.toCreateAdminUserResponse(profile);

    Assertions.assertNotNull(out);
    Assertions.assertEquals(profile.getId(), out.getId());
    Assertions.assertEquals(profile.getUser().getEmail(), out.getEmail());
    Assertions.assertEquals(profile.getFirstName(), out.getFirstName());
    Assertions.assertEquals(profile.getLastName(), out.getLastName());
    Assertions.assertEquals(profile.getProfileImage().getImageURL(), out.getAvatar());
    Assertions.assertEquals(profile.getPhone(), out.getPhone());
    Assertions.assertEquals(profile.getEnabled(), out.getIsActive());
  }

  @Test
  void testConsumerProfile2CreateConsumerUserResponse() {
    User user =
        User.builder()
            .id(10L)
            .email("test@yopmail.com")
            .password("1234")
            .createdAt(LocalDateTime.now())
            .createdBy("somebody1")
            .modifiedAt(LocalDateTime.now())
            .modifiedBy("somebody2")
            .build();
    ConsumerProfile profile =
        new ConsumerProfile(
            15L,
            user,
            ProfileType.builder().id(1L).profile(ProfileTypeEnum.ADMIN).build(),
            true,
            LocalDateTime.now(),
            "somebody",
            LocalDateTime.now(),
            "somebody2",
            null,
            null,
            "John",
            "Dou",
            ProfileImage.builder().id(3L).build(),
            Gender.builder().id(11L).genderName("male").build(),
            "5492494556677",
            Card.builder().id(8L).cardImage(CardImage.builder().build()).build(),
            ZipCode.builder().id(71L).state(State.builder().id(81L).build()).build(),
            true);
    profile.setUserType(ConsumerTypeEnum.MEDICAL);

    CreateConsumerUserResponse out = responsesMapper.toCreateConsumerUserResponse(profile);

    Assertions.assertNotNull(out);
    Assertions.assertEquals(user.getId(), out.getId());
    Assertions.assertEquals(user.getEmail(), out.getEmail());
    Assertions.assertEquals(profile.getFirstName(), out.getFirstName());
    Assertions.assertEquals(profile.getLastName(), out.getLastName());
    Assertions.assertEquals(profile.getProfileImage().getImageURL(), out.getAvatar());
    Assertions.assertEquals(profile.getPhone(), out.getPhone());
    Assertions.assertEquals(profile.getCard().getCardImage().getImageURL(), out.getCardImageURL());
    Assertions.assertEquals(profile.getZipCode().getId(), out.getZipcodeId());
    Assertions.assertEquals(profile.getZipCode().getState().getId(), out.getZipcodeStateId());
    Assertions.assertEquals(ConsumerTypeApiEnum.MEDICAL, out.getUserType());
    Assertions.assertEquals(profile.getEnabled(), out.getIsActive());
    Assertions.assertEquals(profile.getVerified(), out.getVerified());
  }

  @Test
  void testCreateDispensaryUserRequest2DispensaryProfile() {
    CreateDispensaryUserRequest request =
        CreateDispensaryUserRequest.builder()
            .dispensaryId(15L)
            .email("test@yopmail.com")
            .password("1234")
            .build();
    DispensaryProfile out = this.responsesMapper.toCreateDispensaryUserRequest(request);

    Assertions.assertNotNull(out);
    Assertions.assertEquals(request.getDispensaryId(), out.getDispensaryId());
    Assertions.assertEquals(request.getEmail(), out.getUser().getEmail());
    Assertions.assertEquals(request.getPassword(), out.getUser().getPassword());
  }

  @Test
  void testDispensaryProfile2CreateDispensaryUserResponse() {
    DispensaryProfile profile =
        DispensaryProfile.builder()
            .id(17L)
            .dispensaryId(19L)
            .user(User.builder().email("test@yopmail.com").password("1234").build())
            .build();

    CreateDispensaryUserResponse out = this.responsesMapper.toCreateDispensaryUserResponse(profile);

    Assertions.assertNotNull(out);
    Assertions.assertEquals(profile.getId(), out.getId());
    Assertions.assertEquals(profile.getDispensaryId(), out.getDispensaryId());
    Assertions.assertEquals(profile.getUser().getEmail(), out.getEmail());
    Assertions.assertEquals(profile.getEnabled(), out.getIsActive());
  }

  @Test
  void testAdminProfile2GetAdminUserResponse() {
    User user =
        User.builder()
            .id(10L)
            .email("test@yopmail.com")
            .password("1234")
            .createdAt(LocalDateTime.now())
            .createdBy("somebody1")
            .modifiedAt(LocalDateTime.now())
            .modifiedBy("somebody2")
            .build();
    AdminProfile adminProfile =
        AdminProfile.builder()
            .user(user)
            .firstName("John")
            .lastName("Dou")
            .phone("54911134624687")
            .enabled(true)
            .build();
    String url = "/photo/14295867.png";

    GetAdminUserResponse out = this.responsesMapper.toGetAdminUserResponse(adminProfile, url);

    Assertions.assertNotNull(out);
    Assertions.assertEquals(adminProfile.getUser().getId(), out.getUserId());
    Assertions.assertEquals(adminProfile.getFirstName(), out.getFirstName());
    Assertions.assertEquals(adminProfile.getLastName(), out.getLastName());
    Assertions.assertEquals(adminProfile.getUser().getEmail(), out.getEmail());
    Assertions.assertEquals(adminProfile.getPhone(), out.getPhone());
    Assertions.assertEquals(adminProfile.getEnabled(), out.getIsActive());
    Assertions.assertEquals(url, out.getAvatar());
  }

  @Test
  void testConsumerProfile2GetConsumerUserResponse() {
    User user =
        User.builder()
            .id(10L)
            .email("test@yopmail.com")
            .password("1234")
            .createdAt(LocalDateTime.now())
            .createdBy("somebody1")
            .modifiedAt(LocalDateTime.now())
            .modifiedBy("somebody2")
            .build();
    String imageurl = "/photo/lkujhrg908.png";
    String cardurl = "/cards/lkujhrg908.png";
    ConsumerProfile profile =
        ConsumerProfile.builder()
            .user(user)
            .firstName("John")
            .lastName("Dou")
            .profileType(ProfileType.builder().id(13L).profile(ProfileTypeEnum.CONSUMER).build())
            .phone("54911143612345")
            .profileImage(ProfileImage.builder().id(7L).imageURL(imageurl).build())
            .gender(Gender.builder().id(1L).genderName("male").build())
            .enabled(true)
            .card(
                Card.builder()
                    .id(15L)
                    .mmjCard(true)
                    .cardImage(CardImage.builder().id(23L).imageURL(cardurl).build())
                    .build())
            .verified(true)
            .zipCode(
                ZipCode.builder().id(33L).state(State.builder().id(34L).name("CA").build()).build())
            .build();

    GetConsumerUserResponse out = this.responsesMapper.toGetConsumerUserResponse(profile, imageurl);

    Assertions.assertNotNull(out);
    Assertions.assertEquals(profile.getUser().getId(), out.getUserId());
    Assertions.assertEquals(profile.getFirstName(), out.getFirstName());
    Assertions.assertEquals(profile.getLastName(), out.getLastName());
    Assertions.assertEquals(profile.getUser().getId(), out.getUserId());
    Assertions.assertEquals(profile.getUser().getEmail(), out.getEmail());
    Assertions.assertEquals(
        ConsumerTypeApiEnum.findById(profile.getUserType().getId()), out.getUserType());
    Assertions.assertEquals(profile.getPhone(), out.getPhone());
    Assertions.assertEquals(profile.getProfileImage().getImageURL(), out.getAvatar());
    Assertions.assertEquals(profile.getGender().getId(), out.getGenderId());
    Assertions.assertEquals(profile.getEnabled(), out.getIsActive());
    Assertions.assertEquals(profile.getMmjCardImage(), out.getMmjCard());
    Assertions.assertEquals(profile.getVerified(), out.getVerified());
    Assertions.assertEquals(profile.getZipCode().getId(), out.getZipcodeId());
    Assertions.assertEquals(profile.getZipCode().getState().getId(), out.getZipcodeStateId());
  }

  @Test
  void testDispensaryProfile2GetDispensaryUserResponse() {
    User user =
        User.builder()
            .id(10L)
            .email("test@yopmail.com")
            .password("1234")
            .createdAt(LocalDateTime.now())
            .createdBy("somebody1")
            .modifiedAt(LocalDateTime.now())
            .modifiedBy("somebody2")
            .build();
    DispensaryProfile profile =
        DispensaryProfile.builder().user(user).dispensaryId(14L).enabled(true).build();

    GetDispensaryUserResponse out = this.responsesMapper.toGetDispensaryUserResponse(profile);

    Assertions.assertNotNull(out);
    Assertions.assertEquals(profile.getDispensaryId(), out.getDispensaryId());
    Assertions.assertEquals(profile.getUser().getId(), out.getUserId());
    Assertions.assertEquals(profile.getUser().getEmail(), out.getEmail());
    Assertions.assertEquals(profile.getEnabled(), out.getIsActive());
  }

  @Test
  void testAdminProfile2GetAllAdminUsersResponse() {}

  @Test
  void testConsumerProfile2GetAllConsumerUsersResponse() {}

  @Test
  void testDispensaryProfile2GetAllDispensaryUsersResponse() {}

  @Test
  void testUpdateAdminProfileRequest2AdminProfile() {}

  @Test
  void testAdminProfile2UpdateAdminProfileResponse() {}

  @Test
  void testUpdateConsumerProfileRequest2ConsumerProfile() {}

  @Test
  void testConsumerProfile2UpdateConsumerProfileResponse() {}

  @Test
  void testUpdateDispensaryProfileRequest2DispensaryProfile() {}

  @Test
  void testDispensaryProfile2UpdateDispensaryProfileResponse() {}

  @Test
  void testAdminProfile2DeleteAdminProfileResponse() {}

  @Test
  void testConsumerProfile2DeleteConsumerProfileResponse() {}

  @Test
  void testDispensaryProfile2DeleteDispensaryProfileResponse() {}

  @Test
  void testProfile2ActiveStatusProfileResponse() {}

  @Test
  void testGender2GenderResponse() {}

  @Test
  void testCardImage2UpdateCardImageToUploadedStateResponse() {}

  @Test
  void testUser2UserOutbox() {

    User user = new User();
    user.setId(15L);
    user.setEmail("test@yopmail.com");
    user.setPassword("1234");
    user.setCreatedAt(LocalDateTime.now());
    user.setCreatedBy("somebody1");
    user.setModifiedAt(LocalDateTime.now());
    user.setModifiedBy("somebody2");

    UserOutbox out = responsesMapper.toUserOutbox(user);

    Assertions.assertNotNull(out);
    Assertions.assertEquals(15L, out.getId());
    Assertions.assertEquals("test@yopmail.com", out.getEmail());
    Assertions.assertNotNull(out.getCreatedAt());
    Assertions.assertNotNull(out.getModifiedAt());
    Assertions.assertNull(out.getDeletedAt());
  }

  @Test
  void testState2StateResponse() {
    State state = new State();
    state.setId(13L);
    state.setName("California");
    state.setShortName("CA");
    state.setCountry(new Country());
    state.setZipCodes(Set.of(ZipCode.builder().id(10L).code(96543).build()));

    StateResponse out = responsesMapper.toStateResponse(state);

    Assertions.assertNotNull(out);
    Assertions.assertEquals(13L, out.getId());
    Assertions.assertEquals("California", out.getName());
    Assertions.assertEquals("CA", out.getShortName());
  }
}
