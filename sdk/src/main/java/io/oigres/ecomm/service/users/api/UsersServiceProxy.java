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

import io.oigres.ecomm.service.users.Constants;
import io.oigres.ecomm.service.users.Routes;
import io.oigres.ecomm.service.users.api.model.*;
import io.oigres.ecomm.service.users.api.model.admin.*;
import io.oigres.ecomm.service.users.api.model.consumer.*;
import io.oigres.ecomm.service.users.api.model.dispensary.*;
import io.oigres.ecomm.service.users.api.model.exception.InvalidRequestException;
import io.oigres.ecomm.service.users.api.model.exception.NotFoundException;
import io.oigres.ecomm.service.users.api.model.exception.UnauthorizedException;
import io.oigres.ecomm.service.users.api.model.exception.UserTypeNotFoundException;
import io.oigres.ecomm.service.users.api.model.exception.profile.ProfileException;
import io.oigres.ecomm.service.users.api.model.profile.ActiveStatusProfileResponse;
import io.oigres.ecomm.service.users.api.profiles.AsyncAdminUsersService;
import io.oigres.ecomm.service.users.api.profiles.AsyncConsumerUsersService;
import io.oigres.ecomm.service.users.api.profiles.AsyncDispensaryUsersService;
import io.swagger.v3.oas.annotations.Parameter;
import java.net.URI;
import java.util.concurrent.Future;
import java.util.function.Function;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

public class UsersServiceProxy extends MiddlewareProxy
    implements UsersService,
        AsyncUsersService,
        AsyncAdminUsersService,
        AsyncConsumerUsersService,
        AsyncDispensaryUsersService {

  public UsersServiceProxy(WebClient webClient) {
    super(webClient);
  }

  // --------------------------------- getAllUsers --------------------------------- //

  public PageResponse<GetAllUsersResponse> getAllUsers(PageableRequest pageable) {
    return getPage(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH)
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .build(pageable),
        GetAllUsersResponse.class);
  }

  // --------------------------------- createNewAdminUser --------------------------------- //

  @Override
  public CreateAdminUserResponse createNewAdminUser(CreateAdminUserRequest request)
      throws ProfileException {
    return post(
        uriBuilder ->
            uriBuilder.path(Routes.USERS_CONTROLLER_PATH.concat(Routes.PROFILE_ADMIN_USER)).build(),
        request,
        CreateAdminUserResponse.class);
  }

  // --------------------------------- createNewConsumerUser --------------------------------- //

  private Function<UriBuilder, URI> createNewConsumerUser_Call() {
    return uriBuilder ->
        uriBuilder.path(Routes.USERS_CONTROLLER_PATH.concat(Routes.PROFILE_CONSUMER_USER)).build();
  }

  @Override
  public CreateConsumerUserResponse createNewConsumerUser(CreateConsumerUserRequest request)
      throws ProfileException {
    return post(createNewConsumerUser_Call(), request, CreateConsumerUserResponse.class);
  }

  // --------------------------------- createNewDispensaryUser --------------------------------- //

  @Override
  public CreateDispensaryUserResponse createNewDispensaryUser(CreateDispensaryUserRequest request)
      throws ProfileException {
    return post(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.PROFILE_DISPENSARY_USER))
                .build(),
        request,
        CreateDispensaryUserResponse.class);
  }

  // --------------------------------- getUserById --------------------------------- //

  private Function<UriBuilder, URI> getUserById_Call(Long userId) {
    return uriBuilder ->
        uriBuilder.path(Routes.REGION_PATH.concat(Routes.GET_STATE_BY_ID)).build(userId);
  }

  @Override
  public GetUserResponse getUserById(Long userId) throws NotFoundException {
    return get(getUserById_Call(userId), GetUserResponse.class);
  }

  // --------------------------------- getAdminUserById --------------------------------- //

  private Function<UriBuilder, URI> getAdminUserById_Call(Long userId) {
    return uriBuilder ->
        uriBuilder
            .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.GET_PROFILE_ADMIN_USER))
            .build(userId);
  }

  @Override
  public GetAdminUserResponse getAdminUserById(Long userId) throws NotFoundException {
    return get(getAdminUserById_Call(userId), GetAdminUserResponse.class);
  }

  @Override
  public Future<GetAdminUserResponse> getAdminUserByIdAsync(long userId) {
    return getAsync(getAdminUserById_Call(userId), GetAdminUserResponse.class);
  }

  // --------------------------------- getConsumerUserById --------------------------------- //

  private Function<UriBuilder, URI> getConsumerUserById_Call(Long userId) {
    return uriBuilder ->
        uriBuilder
            .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.GET_PROFILE_CONSUMER_USER))
            .build(userId);
  }

  @Override
  public GetConsumerUserResponse getConsumerUserById(Long userId) throws NotFoundException {
    return get(getConsumerUserById_Call(userId), GetConsumerUserResponse.class);
  }

  @Override
  public Future<GetConsumerUserResponse> getConsumerUserByIdAsync(Long userId) {
    return getAsync(getConsumerUserById_Call(userId), GetConsumerUserResponse.class);
  }

  // --------------------------------- getDispensaryUserById --------------------------------- //

  private Function<UriBuilder, URI> getDispensaryUserById_Call(Long userId) {
    return uriBuilder ->
        uriBuilder
            .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.GET_PROFILE_DISPENSARY_USER))
            .build(userId);
  }

  @Override
  public GetDispensaryUserResponse getDispensaryUserById(Long userId) throws NotFoundException {
    return get(getDispensaryUserById_Call(userId), GetDispensaryUserResponse.class);
  }

  @Override
  public Future<GetDispensaryUserResponse> getDispensaryUserByIdAsync(Long userId) {
    return getAsync(getDispensaryUserById_Call(userId), GetDispensaryUserResponse.class);
  }

  // --------------------------------- getDispensaryUserByDispensaryId
  // --------------------------------- //

  private Function<UriBuilder, URI> getDispensaryUserByDispensaryId_Call(Long dispensaryId) {
    return uriBuilder ->
        uriBuilder
            .path(
                Routes.USERS_CONTROLLER_PATH.concat(
                    Routes.GET_PROFILE_DISPENSARY_USER_BY_DISPENSARY_ID))
            .build(dispensaryId);
  }

  @Override
  public GetDispensaryUserResponse getDispensaryUserByDispensaryId(Long dispensaryId)
      throws NotFoundException {
    return get(getDispensaryUserByDispensaryId_Call(dispensaryId), GetDispensaryUserResponse.class);
  }

  @Override
  public Future<GetDispensaryUserResponse> getDispensaryByDispensaryIdAsync(Long dispensaryId) {
    return getAsync(
        getDispensaryUserByDispensaryId_Call(dispensaryId), GetDispensaryUserResponse.class);
  }

  // --------------------------------- getAllAdmins --------------------------------- //

  @Override
  public PageResponse<GetAllAdminUsersResponse> getAllAdmins(PageableRequest pageable) {
    return getPage(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.GET_ALL_PROFILE_ADMIN_USER))
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .build(pageable),
        GetAllAdminUsersResponse.class);
  }

  // --------------------------------- getAllAdmins --------------------------------- //

  @Override
  public PageResponse<GetAllConsumerUsersResponse> getAllConsumers(PageableRequest pageable) {
    return getPage(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.GET_ALL_PROFILE_CONSUMER_USER))
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .build(pageable),
        GetAllConsumerUsersResponse.class);
  }

  // --------------------------------- getAllDispensaries --------------------------------- //

  @Override
  public PageResponse<GetAllDispensaryUsersResponse> getAllDispensaries(PageableRequest pageable) {
    return getPage(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.GET_ALL_PROFILE_DISPENSARY_USER))
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .build(pageable),
        GetAllDispensaryUsersResponse.class);
  }

  // --------------------------------- updateAdmin --------------------------------- //

  @Override
  public UpdateAdminProfileResponse updateAdmin(Long profileId, UpdateAdminProfileRequest request)
      throws NotFoundException {
    return put(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.UPDATE_ADMIN_USER))
                .build(profileId),
        request,
        UpdateAdminProfileResponse.class);
  }

  // --------------------------------- updateConsumer --------------------------------- //

  @Override
  public UpdateConsumerProfileResponse updateConsumer(
      Long userId, UpdateConsumerProfileRequest request) throws NotFoundException {
    return put(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.UPDATE_CONSUMER_USER))
                .build(userId),
        request,
        UpdateConsumerProfileResponse.class);
  }

  // --------------------------------- updateDispensary --------------------------------- //

  @Override
  public UpdateDispensaryProfileResponse updateDispensary(
      Long userId, UpdateDispensaryProfileRequest request) throws NotFoundException {
    return put(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.UPDATE_DISPENSARY_USER))
                .build(userId),
        request,
        UpdateDispensaryProfileResponse.class);
  }

  // --------------------------------- validateUserCommon --------------------------------- //

  private Function<UriBuilder, URI> validateUser_Call() {
    return uriBuilder ->
        uriBuilder.path(Routes.USERS_CONTROLLER_PATH.concat(Routes.VALIDATE_USER)).build();
  }

  @Override
  public ValidateUserResponse validateUser(ValidateUserRequest request)
      throws NotFoundException, UnauthorizedException {
    return post(validateUser_Call(), request, ValidateUserResponse.class);
  }

  @Override
  public Future<ValidateUserResponse> validateUserAsync(ValidateUserRequest request) {
    return postAsync(validateUser_Call(), request, ValidateUserResponse.class);
  }

  // --------------------------------- deleteUserById --------------------------------- //

  @Override
  public DeleteUserResponse deleteUserById(Long userId) {
    return delete(
        uriBuilder ->
            uriBuilder.path(Routes.USERS_CONTROLLER_PATH.concat(Routes.DELETE_USER)).build(userId),
        DeleteUserResponse.class);
  }

  // --------------------------------- deleteAdminUserById --------------------------------- //

  @Override
  public DeleteAdminProfileResponse deleteAdminUserById(Long userId) throws NotFoundException {
    return delete(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.DELETE_ADMIN_USER))
                .build(userId),
        DeleteAdminProfileResponse.class);
  }

  // --------------------------------- deleteConsumerUserById --------------------------------- //

  @Override
  public DeleteConsumerProfileResponse deleteConsumerUserById(Long userId)
      throws NotFoundException {
    return delete(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.DELETE_CONSUMER_USER))
                .build(userId),
        DeleteConsumerProfileResponse.class);
  }

  // --------------------------------- deleteDispensaryUserById --------------------------------- //

  @Override
  public DeleteDispensaryProfileResponse deleteDispensaryUserByDispensaryId(Long dispensaryId)
      throws NotFoundException {
    return delete(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.DELETE_DISPENSARY_USER))
                .build(dispensaryId),
        DeleteDispensaryProfileResponse.class);
  }

  // --------------------------------- activateAdmin --------------------------------- //

  @Override
  public ActiveStatusProfileResponse activateAdmin(Long userId) throws ProfileException {
    return patch(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.ACTIVATE_ADMIN_USER))
                .build(userId),
        ActiveStatusProfileResponse.class);
  }

  // --------------------------------- deactivateAdmin --------------------------------- //

  @Override
  public ActiveStatusProfileResponse deactivateAdmin(Long userId) throws ProfileException {
    return patch(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.DEACTIVATE_ADMIN_USER))
                .build(userId),
        ActiveStatusProfileResponse.class);
  }

  // --------------------------------- activateConsumerUser --------------------------------- //

  @Override
  public ActiveStatusProfileResponse activateConsumerUser(Long userId) {
    return patch(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.ACTIVATE_CONSUMER_USER))
                .build(userId),
        ActiveStatusProfileResponse.class);
  }

  // --------------------------------- deactivateConsumerUser --------------------------------- //

  @Override
  public ActiveStatusProfileResponse deactivateConsumerUser(Long userId) {
    return patch(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.DEACTIVATE_CONSUMER_USER))
                .build(userId),
        ActiveStatusProfileResponse.class);
  }

  // --------------------------------- activateDispensaryUser --------------------------------- //

  @Override
  public ActiveStatusProfileResponse activateDispensaryUser(Long userId) {
    return patch(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.ACTIVATE_DISPENSARY_USER))
                .build(userId),
        ActiveStatusProfileResponse.class);
  }

  // --------------------------------- deactivateDispensaryUser --------------------------------- //

  @Override
  public ActiveStatusProfileResponse deactivateDispensaryUser(Long userId) {
    return patch(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.DEACTIVATE_DISPENSARY_USER))
                .build(userId),
        ActiveStatusProfileResponse.class);
  }

  // --------------------------------- sendCode --------------------------------- //

  @Override
  public void sendCode(SendCodeRequest request) throws NotFoundException, InvalidRequestException {
    post(
        uriBuilder ->
            uriBuilder.path(Routes.USERS_CONTROLLER_PATH.concat(Routes.SEND_CODE)).build(),
        request,
        Void.class);
  }

  // --------------------------------- verifyCode --------------------------------- //

  @Override
  public void verifyCode(VerifyCodeRequest request)
      throws NotFoundException, InvalidRequestException {
    post(
        uriBuilder ->
            uriBuilder.path(Routes.USERS_CONTROLLER_PATH.concat(Routes.VERIFY_CODE)).build(),
        request,
        Void.class);
  }

  // --------------------------------- getAllGenders --------------------------------- //

  @Override
  public PageResponse<GenderResponse> getAllGenders(PageableRequest pageable) {
    return getPage(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.GET_ALL_GENDERS))
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .build(pageable),
        GenderResponse.class);
  }

  // --------------------------------- getGenderById --------------------------------- //

  private Function<UriBuilder, URI> getGenderById_Call(long genderId) {
    return uriBuilder ->
        uriBuilder
            .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.GET_GENDER_BY_ID))
            .build(genderId);
  }

  @Override
  public GenderResponse getGenderById(long genderId)
      throws io.oigres.ecomm.service.users.api.model.exception.GenderNotFoundException {
    return get(getGenderById_Call(genderId), GenderResponse.class);
  }

  @Override
  public Future<GenderResponse> getGenderByIdAsync(long genderId)
      throws io.oigres.ecomm.service.users.api.model.exception.GenderNotFoundException {
    return getAsync(getGenderById_Call(genderId), GenderResponse.class);
  }

  // --------------------------------- getAllUserTypes --------------------------------- //

  @Override
  public PageResponse<UserTypeResponse> getAllUserTypes(
      @Parameter(hidden = true, required = true) PageableRequest pageable) {
    return getPage(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.GET_ALL_USERTYPES))
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .build(pageable),
        UserTypeResponse.class);
  }

  // --------------------------------- getUserTypeById --------------------------------- //

  private Function<UriBuilder, URI> getUserTypeById_Call(long userTypeId) {
    return uriBuilder ->
        uriBuilder
            .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.GET_USERTYPE_BY_ID))
            .build(userTypeId);
  }

  @Override
  public UserTypeResponse getUserTypeById(long userTypeId) throws UserTypeNotFoundException {
    return get(getUserTypeById_Call(userTypeId), UserTypeResponse.class);
  }

  @Override
  public Future<UserTypeResponse> getUserTypeByIdAsync(long userTypeId)
      throws UserTypeNotFoundException {
    return getAsync(getUserTypeById_Call(userTypeId), UserTypeResponse.class);
  }

  // --------------------------------- getStateTaxByUserId --------------------------------- //

  private Function<UriBuilder, URI> getStateTaxByUserId_Call(Long userId) {
    return uriBuilder ->
        uriBuilder.path(Routes.USERS_CONTROLLER_PATH.concat(Routes.GET_USER_STATE)).build(userId);
  }

  @Override
  public GetConsumerStateTax getStateTaxByUserId(Long userId) throws NotFoundException {
    return get(getStateTaxByUserId_Call(userId), GetConsumerStateTax.class);
  }

  @Override
  public ImageUploadLocationResponse getMmjCardImageUploadLocation(String imageExtension) {
    try {
      return getWebClient()
          .get()
          .uri(
              uriBuilder ->
                  uriBuilder
                      .path(
                          Routes.USERS_CONTROLLER_PATH.concat(
                              Routes.GET_MMJCARD_IMAGE_UPLOAD_LOCATION))
                      .queryParam("extension", imageExtension)
                      .build(imageExtension))
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.APPLICATION_JSON)
          .retrieve()
          .onStatus(HttpStatusCode::is4xxClientError, this::error4xxHandling)
          .onStatus(HttpStatusCode::is5xxServerError, this::error5xxHandling)
          .bodyToMono(ImageUploadLocationResponse.class)
          .contextCapture()
          .block();
    } catch (WebClientRequestException e) {
      throw new RuntimeException(Constants.ERROR_500_USER_MESSAGE);
    }
  }

  @Override
  public ImageUploadLocationResponse getAvatarImageUploadLocation(String extension) {
    try {
      return getWebClient()
          .get()
          .uri(
              uriBuilder ->
                  uriBuilder
                      .path(
                          Routes.USERS_CONTROLLER_PATH.concat(
                              Routes.GET_AVATAR_IMAGE_UPLOAD_LOCATION))
                      .queryParam("extension", extension)
                      .build(extension))
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.APPLICATION_JSON)
          .retrieve()
          .onStatus(HttpStatusCode::is4xxClientError, this::error4xxHandling)
          .onStatus(HttpStatusCode::is5xxServerError, this::error5xxHandling)
          .bodyToMono(ImageUploadLocationResponse.class)
          .contextCapture()
          .block();
    } catch (WebClientRequestException e) {
      throw new RuntimeException(Constants.ERROR_500_USER_MESSAGE);
    }
  }

  @Override
  public UpdateCardImageToUploadedStateResponse updateCardImageStatus(String imageUrl) {
    return updateCardImageStatusMono(imageUrl).block();
  }

  private Mono<UpdateCardImageToUploadedStateResponse> updateCardImageStatusMono(String imageUrl) {
    return getWebClient()
        .patch()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.UPLOADS_CARDS))
                    .queryParam("imageUrl", imageUrl)
                    .build())
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(UpdateCardImageToUploadedStateResponse.class)
        .contextCapture();
  }

  @Override
  public Future<GetConsumerStateTax> getStateTaxByUserIdAsync(Long userId)
      throws NotFoundException {
    return getAsync(getStateTaxByUserId_Call(userId), GetConsumerStateTax.class);
  }

  // --------------------------------- changeProfileImageStatus --------------------------------- //

  @Override
  public UpdateProfileImageResponse changeProfileImageStatus(String imageUrl) {
    return patch(
        uriBuilder ->
            uriBuilder
                .path(Routes.USERS_CONTROLLER_PATH.concat(Routes.EDIT_PROFILE_IMAGE_STATUS))
                .queryParam("imageUrl", imageUrl)
                .build(),
        null,
        UpdateProfileImageResponse.class);
  }
}
