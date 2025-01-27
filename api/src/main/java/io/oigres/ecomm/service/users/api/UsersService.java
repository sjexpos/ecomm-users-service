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

package io.oigres.ecomm.service.users.api;

import io.oigres.ecomm.service.users.api.model.*;
import io.oigres.ecomm.service.users.api.model.admin.*;
import io.oigres.ecomm.service.users.api.model.consumer.*;
import io.oigres.ecomm.service.users.api.model.dispensary.*;
import io.oigres.ecomm.service.users.api.model.exception.*;
import io.oigres.ecomm.service.users.api.model.exception.profile.*;
import io.oigres.ecomm.service.users.api.model.profile.ActiveStatusProfileResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

public interface UsersService {

  PageResponse<GetAllUsersResponse> getAllUsers(PageableRequest pageable);

  CreateAdminUserResponse createNewAdminUser(CreateAdminUserRequest request)
      throws ProfileException, ProfileTypeNotFoundException;

  CreateConsumerUserResponse createNewConsumerUser(CreateConsumerUserRequest request)
      throws ProfileException,
          StateNotFoundException,
          GenderNotFoundException,
          ZipcodeNotFoundException,
          UserTypeNotFoundException,
          ProfileTypeNotFoundException;

  CreateDispensaryUserResponse createNewDispensaryUser(CreateDispensaryUserRequest request)
      throws ProfileException, ProfileTypeNotFoundException;

  GetUserResponse getUserById(Long userId) throws NotFoundException;

  GetAdminUserResponse getAdminUserById(Long userId) throws NotFoundException;

  GetConsumerUserResponse getConsumerUserById(Long userId) throws NotFoundException;

  GetDispensaryUserResponse getDispensaryUserById(Long userId) throws NotFoundException;

  GetDispensaryUserResponse getDispensaryUserByDispensaryId(
      @Parameter(name = "dispensaryId", required = true) Long dispensaryId)
      throws NotFoundException;

  PageResponse<GetAllAdminUsersResponse> getAllAdmins(PageableRequest pageable);

  PageResponse<GetAllConsumerUsersResponse> getAllConsumers(PageableRequest pageable);

  PageResponse<GetAllDispensaryUsersResponse> getAllDispensaries(PageableRequest pageable);

  UpdateAdminProfileResponse updateAdmin(
      @Parameter(
              name = "profileId",
              required = true,
              description = "identifier associated with the user (1..N)")
          @Min(value = 1, message = "profileId should be greater than zero")
          Long profileId,
      @RequestBody @Valid UpdateAdminProfileRequest request)
      throws NotFoundException;

  UpdateConsumerProfileResponse updateConsumer(
      @Parameter(
              name = "userId",
              required = true,
              description = "identifier associated with the user (0..N)")
          @Min(value = 0, message = "userId should be greater than " + "zero")
          Long userId,
      @RequestBody @Valid UpdateConsumerProfileRequest request)
      throws NotFoundException;

  UpdateDispensaryProfileResponse updateDispensary(
      @Parameter(
              name = "userId",
              required = true,
              description = "identifier associated with the user (0..N)")
          @Min(value = 0, message = "userId should be greater than " + "zero")
          Long userId,
      @RequestBody @Valid UpdateDispensaryProfileRequest request)
      throws NotFoundException;

  DeleteUserResponse deleteUserById(Long userId) throws NotFoundException;

  DeleteAdminProfileResponse deleteAdminUserById(
      @Parameter(
              name = "userId",
              required = true,
              description = "identifier associated with the user (0..N)")
          @Min(value = 0, message = "userId should be greater than zero")
          Long userId)
      throws NotFoundException;

  DeleteConsumerProfileResponse deleteConsumerUserById(
      @Parameter(
              name = "userId",
              required = true,
              description = "identifier associated with the user (0..N)")
          @Min(value = 0, message = "userId should be greater than zero")
          Long userId)
      throws NotFoundException;

  DeleteDispensaryProfileResponse deleteDispensaryUserByDispensaryId(
      @Parameter(
              name = "dispensaryId",
              required = true,
              description = "identifier associated with the dispensary profile (1..N)")
          @Min(value = 1, message = "dispensaryId should be greater than zero")
          Long dispensaryId)
      throws NotFoundException;

  ActiveStatusProfileResponse activateAdmin(
      @Parameter(
              name = "userId",
              required = true,
              description = "identifier associated with the user (0..N)")
          @Min(value = 1, message = "userId should be greater than zero")
          Long userId)
      throws ProfileException, ProfileNotFoundException, ProfileTypeNotFoundException;

  ActiveStatusProfileResponse deactivateAdmin(
      @Parameter(
              name = "userId",
              required = true,
              description = "identifier associated with the user (0..N)")
          @Min(value = 1, message = "userId should be greater than zero")
          Long userId)
      throws ProfileException, ProfileNotFoundException, ProfileTypeNotFoundException;

  ActiveStatusProfileResponse activateConsumerUser(
      @Parameter(
              name = "userId",
              required = true,
              description = "identifier associated with the user (1..N)")
          @Min(value = 1, message = "userId should be greater than zero")
          Long userId)
      throws ProfileNotFoundException, ProfileEnableStatusExceptionResponseApi;

  ActiveStatusProfileResponse deactivateConsumerUser(
      @Parameter(
              name = "userId",
              required = true,
              description = "identifier associated with the user (1..N)")
          @Min(value = 1, message = "userId should be greater than zero")
          Long userId)
      throws ProfileNotFoundException, ProfileEnableStatusExceptionResponseApi;

  ActiveStatusProfileResponse activateDispensaryUser(
      @Parameter(
              name = "userId",
              required = true,
              description = "identifier associated with the user (1..N)")
          @Min(value = 1, message = "userId should be greater than zero")
          Long userId)
      throws ProfileNotFoundException, ProfileEnableStatusExceptionResponseApi;

  ActiveStatusProfileResponse deactivateDispensaryUser(
      @Parameter(
              name = "userId",
              required = true,
              description = "identifier associated with the user (1..N)")
          @Min(value = 1, message = "userId should be greater than zero")
          Long userId)
      throws ProfileNotFoundException, ProfileEnableStatusExceptionResponseApi;

  ValidateUserResponse validateUser(ValidateUserRequest request)
      throws NotFoundException, UnauthorizedException;

  void sendCode(SendCodeRequest request) throws NotFoundException, InvalidRequestException;

  void verifyCode(VerifyCodeRequest request) throws NotFoundException, InvalidRequestException;

  PageResponse<GenderResponse> getAllGenders(PageableRequest pageable) throws NotFoundException;

  GenderResponse getGenderById(long genderId)
      throws io.oigres.ecomm.service.users.api.model.exception.GenderNotFoundException;

  PageResponse<UserTypeResponse> getAllUserTypes(
      @Parameter(hidden = true, required = true) PageableRequest pageable);

  UserTypeResponse getUserTypeById(long userTypeId) throws UserTypeNotFoundException;

  GetConsumerStateTax getStateTaxByUserId(Long userId) throws NotFoundException;

  UpdateProfileImageResponse changeProfileImageStatus(String imageUrl);

  ImageUploadLocationResponse getMmjCardImageUploadLocation(
      @Parameter(description = "File extension which will be uploaded", required = true)
          String imageExtension);

  ImageUploadLocationResponse getAvatarImageUploadLocation(
      @Parameter(description = "File extension which will be uploaded", required = true)
          String extension);

  UpdateCardImageToUploadedStateResponse updateCardImageStatus(String imageUrl);
}
