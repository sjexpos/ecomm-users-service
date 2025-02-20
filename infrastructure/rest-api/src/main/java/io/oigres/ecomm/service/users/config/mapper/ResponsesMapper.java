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

import io.oigres.ecomm.service.users.api.model.GenderResponse;
import io.oigres.ecomm.service.users.api.model.GetAllUsersResponse;
import io.oigres.ecomm.service.users.api.model.UpdateCardImageToUploadedStateResponse;
import io.oigres.ecomm.service.users.api.model.admin.*;
import io.oigres.ecomm.service.users.api.model.consumer.*;
import io.oigres.ecomm.service.users.api.model.dispensary.*;
import io.oigres.ecomm.service.users.api.model.enums.ConsumerTypeApiEnum;
import io.oigres.ecomm.service.users.api.model.profile.ActiveStatusProfileResponse;
import io.oigres.ecomm.service.users.api.model.region.StateResponse;
import io.oigres.ecomm.service.users.api.model.region.ZipCodeResponse;
import io.oigres.ecomm.service.users.api.txoutbox.UserOutbox;
import io.oigres.ecomm.service.users.domain.*;
import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.enums.ConsumerTypeEnum;
import io.oigres.ecomm.service.users.usecases.users.utils.ImageUtils;
import java.util.List;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ResponsesMapper {

  public GetAllUsersResponse toGetAllUsersResponse(User user) {
    if (user == null) {
      return null;
    }
    return GetAllUsersResponse.builder().userId(user.getId()).email(user.getEmail()).build();
  }

  public abstract List<GetAllUsersResponse> toGetAllUsersResponse(List<User> users);

  public AdminProfile toAdminProfile(CreateAdminUserRequest request) {
    if (request == null) {
      return null;
    }
    User user = User.builder().email(request.getEmail()).password(request.getPassword()).build();
    ProfileImage profileImage = ProfileImage.builder().imageURL(request.getAvatar()).build();
    return AdminProfile.builder()
        .user(user)
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .phone(request.getPhone())
        .profileImage(profileImage)
        .build();
  }

  public CreateAdminUserResponse toCreateAdminUserResponse(AdminProfile profile) {
    if (profile == null) {
      return null;
    }
    return CreateAdminUserResponse.builder()
        .id(profile.getId())
        .firstName(profile.getFirstName())
        .lastName(profile.getLastName())
        .phone(profile.getPhone())
        .isActive(profile.getEnabled())
        .avatar(profile.getProfileImage().getImageURL())
        .email(profile.getUser().getEmail())
        .build();
  }

  public CreateConsumerUserResponse toCreateConsumerUserResponse(ConsumerProfile profile) {
    if (profile == null) {
      return null;
    }
    return CreateConsumerUserResponse.builder()
        .id(Objects.isNull(profile.getUser()) ? null : profile.getUser().getId())
        .email(Objects.isNull(profile.getUser()) ? null : profile.getUser().getEmail())
        .firstName(profile.getFirstName())
        .lastName(profile.getLastName())
        .avatar(
            Objects.isNull(profile.getProfileImage())
                ? null
                : profile.getProfileImage().getImageURL())
        .phone(profile.getPhone())
        .cardImageURL(
            Objects.isNull(profile.getCard()) || Objects.isNull(profile.getCard().getCardImage())
                ? null
                : profile.getCard().getCardImage().getImageURL())
        .zipcodeId(Objects.isNull(profile.getZipCode()) ? null : profile.getZipCode().getId())
        .zipcodeStateId(
            Objects.isNull(profile.getZipCode()) || Objects.isNull(profile.getZipCode().getState())
                ? null
                : profile.getZipCode().getState().getId())
        .userType(
            Objects.isNull(profile.getUserType())
                ? null
                : ConsumerTypeApiEnum.findById(profile.getUserType().getId()))
        .isActive(profile.getEnabled())
        .verified(profile.getVerified())
        .build();
  }

  public DispensaryProfile toCreateDispensaryUserRequest(CreateDispensaryUserRequest request) {
    if (request == null) {
      return null;
    }
    User user = User.builder().email(request.getEmail()).password(request.getPassword()).build();
    return DispensaryProfile.builder().dispensaryId(request.getDispensaryId()).user(user).build();
  }

  public CreateDispensaryUserResponse toCreateDispensaryUserResponse(DispensaryProfile profile) {
    if (profile == null) {
      return null;
    }
    return CreateDispensaryUserResponse.builder()
        .dispensaryId(profile.getDispensaryId())
        .id(profile.getId())
        .email(profile.getUser().getEmail())
        .isActive(profile.getEnabled())
        .build();
  }

  public GetAdminUserResponse toGetAdminUserResponse(AdminProfile adminProfile, String url) {
    if (adminProfile == null) {
      return null;
    }
    return GetAdminUserResponse.builder()
        .userId(adminProfile.getUser().getId())
        .email(adminProfile.getUser().getEmail())
        .phone(adminProfile.getPhone())
        .firstName(adminProfile.getFirstName())
        .lastName(adminProfile.getLastName())
        .isActive(adminProfile.getEnabled())
        .avatar(url)
        .build();
  }

  public GetConsumerUserResponse toGetConsumerUserResponse(ConsumerProfile profile, String url) {
    if (profile == null) {
      return null;
    }
    return GetConsumerUserResponse.builder()
        .userId(profile.getUser().getId())
        .firstName(profile.getFirstName())
        .lastName(profile.getLastName())
        .email(profile.getUser().getEmail())
        .phone(profile.getPhone())
        .isActive(profile.getEnabled())
        .avatar(profile.getProfileImage().getImageURL())
        .userType(ConsumerTypeApiEnum.findById(profile.getUserType().getId()))
        .genderId(profile.getGender().getId())
        .mmjCard(profile.getMmjCardImage())
        .zipcodeId(profile.getZipCode().getId())
        .zipcodeStateId(profile.getZipCode().getState().getId())
        .verified(profile.getVerified())
        .avatar(url)
        .build();
  }

  public GetDispensaryUserResponse toGetDispensaryUserResponse(DispensaryProfile profile) {
    if (profile == null) {
      return null;
    }
    return GetDispensaryUserResponse.builder()
        .dispensaryId(profile.getDispensaryId())
        .userId(profile.getUser().getId())
        .email(profile.getUser().getEmail())
        .isActive(profile.getEnabled())
        .build();
  }

  public GetAllAdminUsersResponse toGetAllAdminUsersResponse(
      AdminProfile adminProfile, String url) {
    if (adminProfile == null) {
      return null;
    }
    return GetAllAdminUsersResponse.builder()
        .userId(adminProfile.getUser().getId())
        .email(adminProfile.getUser().getEmail())
        .phone(adminProfile.getPhone())
        .firstName(adminProfile.getFirstName())
        .lastName(adminProfile.getLastName())
        .isActive(adminProfile.getEnabled())
        .avatar(url)
        .build();
  }

  public GetAllConsumerUsersResponse toGetAllConsumerUsersResponse(ConsumerProfile cp) {
    if (cp == null) {
      return null;
    }
    return GetAllConsumerUsersResponse.builder()
        .userId(cp.getUser().getId())
        .email(cp.getUser().getEmail())
        .firstName(cp.getFirstName())
        .lastName(cp.getLastName())
        .avatar(ImageUtils.getProfileImageURLForConsumerUser(cp))
        .gender(cp.getGender().getGenderName())
        .phone(cp.getPhone())
        .state(cp.getZipCode().getState().getName())
        .zipCode(cp.getZipCode().getCode().toString())
        .userType(cp.getUserType().getPrettyName())
        .isActive(cp.getEnabled())
        .verified(cp.getVerified())
        .build();
  }

  public abstract List<GetAllConsumerUsersResponse> toGetAllConsumerUsersResponse(
      List<ConsumerProfile> profiles);

  public GetAllDispensaryUsersResponse toGetAllDispensaryUsersResponse(
      DispensaryProfile dispensaryProfile) {
    if (dispensaryProfile == null) {
      return null;
    }
    return GetAllDispensaryUsersResponse.builder()
        .dispensaryId(dispensaryProfile.getDispensaryId())
        .userId(dispensaryProfile.getUser().getId())
        .email(dispensaryProfile.getUser().getEmail())
        .isActive(dispensaryProfile.getEnabled())
        .build();
  }

  public abstract List<GetAllDispensaryUsersResponse> toGetAllDispensaryUsersResponse(
      List<DispensaryProfile> profiles);

  public AdminProfile toAdminProfile(UpdateAdminProfileRequest request) {
    if (request == null) {
      return null;
    }
    return AdminProfile.builder()
        .user(User.builder().email(request.getEmail()).password(request.getPassword()).build())
        .phone(request.getPhone())
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .profileImage(ProfileImage.builder().imageURL(request.getImg()).build())
        .build();
  }

  public UpdateAdminProfileResponse toUpdateAdminProfileResponse(AdminProfile profile) {
    if (profile == null) {
      return null;
    }
    return UpdateAdminProfileResponse.builder()
        .id(profile.getId())
        .email(profile.getUser().getEmail())
        .img(profile.getProfileImage().getImageURL())
        .phone(profile.getPhone())
        .firstName(profile.getFirstName())
        .lastName(profile.getLastName())
        .isActive(profile.getEnabled())
        .build();
  }

  public ConsumerProfile toConsumerProfile(UpdateConsumerProfileRequest request) {
    if (request == null) {
      return null;
    }
    ConsumerProfile profile =
        ConsumerProfile.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .phone(request.getPhone())
            .profileImage(ProfileImage.builder().imageURL(request.getAvatar()).build())
            .zipCode(
                ZipCode.builder()
                    .id(request.getZipcodeId())
                    .state(State.builder().id(request.getZipcodeStateId()).build())
                    .build())
            .card(
                Card.builder()
                    .cardImage(CardImage.builder().imageURL(request.getCardImageURL()).build())
                    .build())
            .gender(Gender.builder().id(request.getGenderId()).build())
            .user(User.builder().email(request.getPassword()).build())
            .build();
    profile.setUserType(ConsumerTypeEnum.getById(request.getUserType().getId()).orElse(null));
    return profile;
  }

  public UpdateConsumerProfileResponse toUpdateConsumerProfileResponse(ConsumerProfile profile) {
    if (profile == null) {
      return null;
    }
    return UpdateConsumerProfileResponse.builder()
        .id(profile.getId())
        .firstName(profile.getFirstName())
        .lastName(profile.getLastName())
        .email(profile.getUser().getEmail())
        .genderId(profile.getGender().getId())
        .isActive(profile.getEnabled())
        .mmjCard(profile.getMmjCardImage())
        .phone(profile.getPhone())
        .userType(ConsumerTypeApiEnum.findById(profile.getUserType().getId()))
        .verified(profile.getVerified())
        .zipcodeId(profile.getZipCode().getId())
        .zipcodeStateId(profile.getZipCode().getState().getId())
        .avatar(profile.getProfileImage() != null ? profile.getProfileImage().getImageURL() : null)
        .build();
  }

  public DispensaryProfile toDispensaryProfile(UpdateDispensaryProfileRequest request) {
    if (request == null) {
      return null;
    }
    return DispensaryProfile.builder()
        .user(User.builder().password(request.getPassword()).build())
        .dispensaryId(request.getDispensaryId())
        .build();
  }

  public UpdateDispensaryProfileResponse toUpdateDispensaryProfileResponse(
      DispensaryProfile profile) {
    if (profile == null) {
      return null;
    }
    return UpdateDispensaryProfileResponse.builder()
        .dispensaryId(profile.getDispensaryId())
        .email(profile.getUser().getEmail())
        .id(profile.getId())
        .isActive(profile.getEnabled())
        .build();
  }

  public DeleteAdminProfileResponse toDeleteAdminProfileResponse(AdminProfile profile) {
    if (profile == null) {
      return null;
    }
    return DeleteAdminProfileResponse.builder()
        .id(profile.getId())
        .firstName(profile.getFirstName())
        .lastName(profile.getLastName())
        .phone(profile.getPhone())
        .isActive(profile.getEnabled())
        .email(profile.getUser().getEmail())
        .avatar(profile.getProfileImage().getImageURL())
        .build();
  }

  public DeleteConsumerProfileResponse toDeleteConsumerProfileResponse(ConsumerProfile profile) {
    if (profile == null) {
      return null;
    }
    return DeleteConsumerProfileResponse.builder()
        .id(profile.getId())
        .email(profile.getUser().getEmail())
        .firstName(profile.getFirstName())
        .lastName(profile.getLastName())
        .phone(profile.getPhone())
        .avatar(profile.getProfileImage().getImageURL())
        .gender(profile.getGender().getGenderName())
        .isActive(profile.getEnabled())
        .mmjCard(profile.getMmjCardImage())
        .zipCode(profile.getZipCode().getCity())
        .state(profile.getZipCode().getState().getName())
        .userType(profile.getUserType().getPrettyName())
        .verified(profile.getVerified())
        .build();
  }

  public DeleteDispensaryProfileResponse toDeleteDispensaryProfileResponse(
      DispensaryProfile profile) {
    if (profile == null) {
      return null;
    }
    return DeleteDispensaryProfileResponse.builder()
        .dispensaryId(profile.getDispensaryId())
        .id(profile.getId())
        .email(profile.getUser().getEmail())
        .isActive(profile.getEnabled())
        .build();
  }

  public ActiveStatusProfileResponse toActiveStatusProfileResponse(Profile profile) {
    if (profile == null) {
      return null;
    }
    return ActiveStatusProfileResponse.builder()
        .id(profile.getUser().getId())
        .enabled(profile.getEnabled())
        .build();
  }

  public GenderResponse toGenderResponse(Gender gender) {
    if (gender == null) {
      return null;
    }
    return GenderResponse.builder().id(gender.getId()).genderName(gender.getGenderName()).build();
  }

  public abstract List<GenderResponse> toGenderResponse(List<Gender> genders);

  public UpdateCardImageToUploadedStateResponse toUpdateCardImageToUploadedStateResponse(
      CardImage cardImage) {
    if (cardImage == null) {
      return null;
    }
    return UpdateCardImageToUploadedStateResponse.builder()
        .id(cardImage.getId())
        .imageURL(cardImage.getImageURL())
        .status(cardImage.getStatus().name())
        .build();
  }

  public abstract UserOutbox toUserOutbox(User user);

  public abstract StateResponse toStateResponse(State state);

  public abstract List<StateResponse> toStateResponse(List<State> states);

  public abstract ZipCodeResponse toZipCodeResponse(ZipCode zipCode);

  public abstract List<ZipCodeResponse> toZipCodeResponse(List<ZipCode> zipCodes);
}
