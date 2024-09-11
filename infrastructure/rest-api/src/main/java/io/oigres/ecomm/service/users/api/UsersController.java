package io.oigres.ecomm.service.users.api;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.oigres.ecomm.service.users.Routes;
import io.oigres.ecomm.service.users.api.model.*;
import io.oigres.ecomm.service.users.api.model.admin.*;
import io.oigres.ecomm.service.users.api.model.consumer.*;
import io.oigres.ecomm.service.users.api.model.dispensary.*;
import io.oigres.ecomm.service.users.api.model.exception.InvalidRequestException;
import io.oigres.ecomm.service.users.api.model.exception.NotFoundException;
import io.oigres.ecomm.service.users.api.model.exception.UnauthorizedException;
import io.oigres.ecomm.service.users.api.model.exception.UserTypeNotFoundException;
import io.oigres.ecomm.service.users.api.model.exception.profile.*;
import io.oigres.ecomm.service.users.api.model.profile.ActiveStatusProfileResponse;
import io.oigres.ecomm.service.users.domain.*;
import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.enums.BlobType;
import io.oigres.ecomm.service.users.enums.ConsumerTypeEnum;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;
import io.oigres.ecomm.service.users.exception.*;
import io.oigres.ecomm.service.users.exception.profile.*;
import io.oigres.ecomm.service.users.usecases.uploads.GetStoreLocationUseCase;
import io.oigres.ecomm.service.users.usecases.users.admins.create.CreateNewAdminUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.admins.delete.DeleteAdminUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.admins.enable.DisableAdminUseCase;
import io.oigres.ecomm.service.users.usecases.users.admins.enable.EnableAdminUseCase;
import io.oigres.ecomm.service.users.usecases.users.admins.list.GetAllAdminsUseCase;
import io.oigres.ecomm.service.users.usecases.users.admins.search.GetAdminByIdUseCase;
import io.oigres.ecomm.service.users.usecases.users.admins.update.UpdateAdminUseCase;
import io.oigres.ecomm.service.users.usecases.users.consumers.create.CreateNewConsumerUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.consumers.delete.DeleteConsumerUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.consumers.list.GetAllConsumersUseCase;
import io.oigres.ecomm.service.users.usecases.users.consumers.search.GetConsumerByIdUseCase;
import io.oigres.ecomm.service.users.usecases.users.consumers.update.UpdateConsumerUseCase;
import io.oigres.ecomm.service.users.usecases.users.delete.DeleteUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.dispensaries.create.CreateNewDispensaryUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.dispensaries.delete.DeleteDispensaryUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.dispensaries.list.GetAllDispensariesUseCase;
import io.oigres.ecomm.service.users.usecases.users.dispensaries.search.GetDispensaryByDispensaryIdUseCase;
import io.oigres.ecomm.service.users.usecases.users.dispensaries.search.GetDispensaryByIdUseCase;
import io.oigres.ecomm.service.users.usecases.users.dispensaries.update.UpdateDispensaryUseCase;
import io.oigres.ecomm.service.users.usecases.users.gender.GetAllGendersUseCase;
import io.oigres.ecomm.service.users.usecases.users.gender.GetGenderByIdUseCase;
import io.oigres.ecomm.service.users.usecases.users.images.ChangeCardImageStatusUseCase;
import io.oigres.ecomm.service.users.usecases.users.images.ChangeProfileImageStatusUseCase;
import io.oigres.ecomm.service.users.usecases.users.images.model.UpdateProfileImageDTO;
import io.oigres.ecomm.service.users.usecases.users.list.GetAllUsersUseCase;
import io.oigres.ecomm.service.users.usecases.users.profiles.ToggleEnabledProfileUseCaseImpl;
import io.oigres.ecomm.service.users.usecases.users.search.GetUserByIdUseCase;
import io.oigres.ecomm.service.users.usecases.users.sendcode.SendCodeUseCase;
import io.oigres.ecomm.service.users.usecases.users.utils.ImageUtils;
import io.oigres.ecomm.service.users.usecases.users.validate.ValidateUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.verifycode.VerifyCodeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

import org.modelmapper.ModelMapper;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Routes.USERS_CONTROLLER_PATH)
@Tag(name = "Users")
@Slf4j
public class UsersController extends AbstractController implements UsersService {

    private final CreateNewAdminUserUseCase createNewAdminUserUseCase;
    private final CreateNewConsumerUserUseCase createNewConsumerUserUseCase;
    private final CreateNewDispensaryUserUseCase createNewDispensaryUserUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetAdminByIdUseCase getAdminByIdUseCase;
    private final GetConsumerByIdUseCase getConsumerByIdUseCase;
    private final GetDispensaryByIdUseCase getDispensaryByIdUseCase;
    private final GetDispensaryByDispensaryIdUseCase getDispensaryByDispensaryIdUseCase;
    private final GetAllAdminsUseCase getAllAdminsUseCase;
    private final GetAllConsumersUseCase getAllConsumersUseCase;
    private final GetAllDispensariesUseCase getAllDispensariesUseCase;
    private final UpdateAdminUseCase updateAdminUseCase;
    private final UpdateConsumerUseCase updateConsumerUseCase;
    private final UpdateDispensaryUseCase updateDispensaryUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final DeleteAdminUserUseCase deleteAdminUserUseCase;
    private final DeleteConsumerUserUseCase deleteConsumerUserUseCase;
    private final DeleteDispensaryUserUseCase deleteDispensaryUserUseCase;
    private final ValidateUserUseCase validateUserUseCase;
    private final SendCodeUseCase sendCodeUseCase;
    private final VerifyCodeUseCase verifyCodeUseCase;
    private final GetAllGendersUseCase getAllGendersUseCase;
    private final GetGenderByIdUseCase getGenderByIdUseCase;
    private final EnableAdminUseCase enableAdminUseCase;
    private final DisableAdminUseCase disableAdminUseCase;
    private final ToggleEnabledProfileUseCaseImpl toggleEnabledProfileUseCaseImpl;
    private final GetStoreLocationUseCase getStoreLocationUseCase;
    private final ChangeProfileImageStatusUseCase changeProfileImageStatusUseCase;
    private final ChangeCardImageStatusUseCase changeCardImageStatusUseCase;
    private final ModelMapper modelMapper;

    public UsersController(CreateNewAdminUserUseCase createNewAdminUserUseCase,
                           CreateNewConsumerUserUseCase createNewConsumerUserUseCase,
                           CreateNewDispensaryUserUseCase createNewDispensaryUserUseCase,
                           GetAllUsersUseCase getAllUsersUseCase,
                           GetUserByIdUseCase getUserByIdUseCase,
                           GetAdminByIdUseCase getAdminByIdUseCase,
                           GetConsumerByIdUseCase getConsumerByIdUseCase,
                           GetDispensaryByIdUseCase getDispensaryByIdUseCase,
                           GetDispensaryByDispensaryIdUseCase getDispensaryByDispensaryIdUseCase,
                           GetAllAdminsUseCase getAllAdminsUseCase,
                           GetAllConsumersUseCase getAllConsumersUseCase,
                           GetAllDispensariesUseCase getAllDispensariesUseCase,
                           UpdateAdminUseCase updateAdminUseCase,
                           UpdateConsumerUseCase updateConsumerUseCase,
                           UpdateDispensaryUseCase updateDispensaryUseCase,
                           DeleteUserUseCase deleteUserUseCase,
                           DeleteAdminUserUseCase deleteAdminUserUseCase,
                           DeleteConsumerUserUseCase deleteConsumerUserUseCase,
                           DeleteDispensaryUserUseCase deleteDispensaryUserUseCase,
                           ValidateUserUseCase validateUserUseCase,
                           SendCodeUseCase sendCodeUseCase,
                           VerifyCodeUseCase verifyCodeUseCase,
                           GetAllGendersUseCase getAllGendersUseCase,
                           GetGenderByIdUseCase getGenderByIdUseCase,
                           EnableAdminUseCase enableAdminUseCase,
                           DisableAdminUseCase disableAdminUseCase,
                           ToggleEnabledProfileUseCaseImpl toggleEnabledProfileUseCaseImpl,
                           GetStoreLocationUseCase getStoreLocationUseCase,
                           ChangeProfileImageStatusUseCase changeProfileImageStatusUseCase,
                           ChangeCardImageStatusUseCase changeCardImageStatusUseCase,
                           ModelMapper modelMapper) {
        this.createNewAdminUserUseCase = createNewAdminUserUseCase;
        this.createNewConsumerUserUseCase = createNewConsumerUserUseCase;
        this.createNewDispensaryUserUseCase = createNewDispensaryUserUseCase;
        this.getAllUsersUseCase = getAllUsersUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.getAdminByIdUseCase = getAdminByIdUseCase;
        this.getConsumerByIdUseCase = getConsumerByIdUseCase;
        this.getDispensaryByIdUseCase = getDispensaryByIdUseCase;
        this.getDispensaryByDispensaryIdUseCase = getDispensaryByDispensaryIdUseCase;
        this.getAllAdminsUseCase = getAllAdminsUseCase;
        this.getAllConsumersUseCase = getAllConsumersUseCase;
        this.getAllDispensariesUseCase = getAllDispensariesUseCase;
        this.updateAdminUseCase = updateAdminUseCase;
        this.updateConsumerUseCase = updateConsumerUseCase;
        this.updateDispensaryUseCase = updateDispensaryUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.deleteAdminUserUseCase = deleteAdminUserUseCase;
        this.deleteConsumerUserUseCase = deleteConsumerUserUseCase;
        this.deleteDispensaryUserUseCase = deleteDispensaryUserUseCase;
        this.validateUserUseCase = validateUserUseCase;
        this.sendCodeUseCase = sendCodeUseCase;
        this.verifyCodeUseCase = verifyCodeUseCase;
        this.getAllGendersUseCase = getAllGendersUseCase;
        this.getGenderByIdUseCase = getGenderByIdUseCase;
        this.enableAdminUseCase = enableAdminUseCase;
        this.disableAdminUseCase = disableAdminUseCase;
        this.toggleEnabledProfileUseCaseImpl = toggleEnabledProfileUseCaseImpl;
        this.getStoreLocationUseCase = getStoreLocationUseCase;
        this.changeProfileImageStatusUseCase = changeProfileImageStatusUseCase;
        this.changeCardImageStatusUseCase = changeCardImageStatusUseCase;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get all users")
    @PageableAsQueryParam
    @GetMapping(produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @RateLimiter(name = "get-all-users-endpoint")
    public PageResponse<GetAllUsersResponse> getAllUsers(@Parameter(hidden = true) @PageableDefault(page = 0, size = 40) PageableRequest pageable) {
        log.info("############ call getAllUsers ############");
        Page<User> users = this.getAllUsersUseCase.handle(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending()));
        List<GetAllUsersResponse> response = users.getContent().stream().map(u -> GetAllUsersResponse.builder().userId(u.getId()).email(u.getEmail()).build()).collect(Collectors.toList());
        return new PageResponseImpl<>(response, pageable, users.getTotalElements());
    }

    @Operation(summary = "Create a new Admin user")
    @PostMapping(value = Routes.PROFILE_ADMIN_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    @RateLimiter(name = "create-new-admin-user-endpoint")
    public CreateAdminUserResponse createNewAdminUser(@RequestBody @Valid CreateAdminUserRequest request) throws ProfileException {
        log.info("############ call createNewAdminUser [{}] ############", request.getEmail());
        try {
            AdminProfile adminProfile = this.createNewAdminUserUseCase.handle(modelMapper.map(request, AdminProfile.class));
            return modelMapper.map(adminProfile, CreateAdminUserResponse.class);
        } catch (DeletedProfileException e) {
            throw new ProfileDeletedException(e.getMessage());
        } catch (ExistingProfileException e) {
            throw new ProfileExistingException(e.getMessage());
        } catch (TypeNotFoundProfileException e) {
            throw new ProfileTypeNotFoundException(e.getMessage());
        }
    }

    @Operation(summary = "Create a new Consumer")
    @PostMapping(value = Routes.PROFILE_CONSUMER_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public CreateConsumerUserResponse createNewConsumerUser(@RequestBody @Valid CreateConsumerUserRequest request) throws ProfileException, io.oigres.ecomm.service.users.api.model.exception.StateNotFoundException, io.oigres.ecomm.service.users.api.model.exception.GenderNotFoundException, io.oigres.ecomm.service.users.api.model.exception.ZipcodeNotFoundException, UserTypeNotFoundException {
        log.info("############ call createNewConsumerUser [{}] ############", request.getEmail());
        try {
            ConsumerProfile consumerProfile = this.createNewConsumerUserUseCase.handle(request);
            return modelMapper.map(consumerProfile, CreateConsumerUserResponse.class);
        } catch (DeletedProfileException e) {
            throw new ProfileDeletedException(e.getMessage());
        } catch (ExistingProfileException e) {
            throw new ProfileExistingException(e.getMessage());
        } catch (TypeNotFoundProfileException e) {
            throw new ProfileTypeNotFoundException(e.getMessage());
        } catch (StateNotFoundException e) {
            throw new io.oigres.ecomm.service.users.api.model.exception.StateNotFoundException(e.getMessage());
        } catch (GenderNotFoundException e) {
            throw new io.oigres.ecomm.service.users.api.model.exception.GenderNotFoundException(e.getMessage());
        } catch (ZipcodeNotFoundDomainException e) {
            throw new io.oigres.ecomm.service.users.api.model.exception.ZipcodeNotFoundException(e.getMessage());
        } catch (io.oigres.ecomm.service.users.exception.profile.UserTypeNotFoundException e) {
            throw new UserTypeNotFoundException(e.getMessage());
        }
    }

    @Operation(summary = "Create a new Dispensary user")
    @PostMapping(value = Routes.PROFILE_DISPENSARY_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public CreateDispensaryUserResponse createNewDispensaryUser(@RequestBody @Valid CreateDispensaryUserRequest request) throws ProfileException {
        log.info("############ call createNewDispensaryUser [{}, {}] ############", request.getEmail(), request.getDispensaryId());
        try {
            DispensaryProfile dispensaryProfile = this.createNewDispensaryUserUseCase.handle(modelMapper.map(request, DispensaryProfile.class));
            return modelMapper.map(dispensaryProfile, CreateDispensaryUserResponse.class);
        } catch (DeletedProfileException e) {
            throw new ProfileDeletedException(e.getMessage());
        } catch (ExistingProfileException e) {
            throw new ProfileExistingException(e.getMessage());
        } catch (TypeNotFoundProfileException e) {
            throw new ProfileTypeNotFoundException(e.getMessage());
        }
    }

    @Operation(summary = "Retrieve information for a user")
    @GetMapping(value = Routes.GET_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Deprecated
    public GetUserResponse getUserById(@PathVariable("userId") Long userId) throws NotFoundException {
        log.info("############ call getUserById [{}] ############", userId);
        User user;
        try {
            user = this.getUserByIdUseCase.handle(userId);
        } catch (UserNotFoundException e) {
            throw new NotFoundException();
        }
        return GetUserResponse.builder().userId(user.getId()).build();
    }

    @Operation(summary = "Retrieve information for an Admin")
    @GetMapping(value = Routes.GET_PROFILE_ADMIN_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public GetAdminUserResponse getAdminUserById(@PathVariable("userId") Long userId) throws NotFoundException {
        log.info("############ call getAdminUserById [{}] ############", userId);
        try {
            AdminProfile adminProfile = getAdminByIdUseCase.handle(userId);
            String url = ImageUtils.getProfileImageURLForAdminUser(adminProfile);
            GetAdminUserResponse response = modelMapper.map(adminProfile, GetAdminUserResponse.class);
            response.setAvatar(url);
            return response;
        } catch (NotFoundProfileException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Operation(summary = "Retrieve information for a Consumer")
    @GetMapping(value = Routes.GET_PROFILE_CONSUMER_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public GetConsumerUserResponse getConsumerUserById(@PathVariable("userId") Long userId) throws NotFoundException {
        log.info("############ call getConsumerUserById [{}] ############", userId);
        try {
            ConsumerProfile cp = getConsumerByIdUseCase.handle(userId);
            String url = ImageUtils.getProfileImageURLForConsumerUser(cp);
            GetConsumerUserResponse response = modelMapper.map(cp, GetConsumerUserResponse.class);
            response.setAvatar(url);
            return response;
        } catch (NotFoundProfileException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Operation(summary = "Retrieve information for a Dispensary")
    @GetMapping(value = Routes.GET_PROFILE_DISPENSARY_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public GetDispensaryUserResponse getDispensaryUserById(@PathVariable("userId") Long userId) throws NotFoundException {
        log.info("############ call getDispensaryUserById [{}] ############", userId);
        try {
            DispensaryProfile dispensaryProfile = getDispensaryByIdUseCase.handle(userId);
            return modelMapper.map(dispensaryProfile, GetDispensaryUserResponse.class);
        } catch (NotFoundProfileException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Operation(summary = "Retrieve information for a Dispensary")
    @GetMapping(value = Routes.GET_PROFILE_DISPENSARY_USER_BY_DISPENSARY_ID, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public GetDispensaryUserResponse getDispensaryUserByDispensaryId(@PathVariable("dispensaryId") Long dispensaryId) throws NotFoundException {
        log.info("############ call getDispensaryUserByDispensaryId [{}] ############", dispensaryId);
        try {
            DispensaryProfile dispensaryProfile = getDispensaryByDispensaryIdUseCase.handle(dispensaryId);
            return modelMapper.map(dispensaryProfile, GetDispensaryUserResponse.class);
        } catch (NotFoundProfileException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Operation(summary = "Get all Admins")
    @PageableAsQueryParam
    @GetMapping(value = Routes.GET_ALL_PROFILE_ADMIN_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PageResponse<GetAllAdminUsersResponse> getAllAdmins(@Parameter(hidden = true, required = true) PageableRequest pageable) {
        log.info("############ call getAllAdmins ############");
        Page<AdminProfile> adminProfilePage = getAllAdminsUseCase.handle(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending()));
        List<GetAllAdminUsersResponse> getAllAdminUsersResponseList = adminProfilePage.getContent().stream()
                .map(adminProfile -> {
                    GetAllAdminUsersResponse response = modelMapper.map(adminProfile, GetAllAdminUsersResponse.class);
                    String url = ImageUtils.getProfileImageURLForAdminUser(adminProfile);
                    response.setAvatar(url);
                    return response;
                })
                .collect(Collectors.toList());
        return new PageResponseImpl<>(getAllAdminUsersResponseList, pageable, adminProfilePage.getTotalElements());
    }

    @Operation(summary = "Get all Consumers")
    @PageableAsQueryParam
    @GetMapping(value = Routes.GET_ALL_PROFILE_CONSUMER_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PageResponse<GetAllConsumerUsersResponse> getAllConsumers(@Parameter(hidden = true, required = true) PageableRequest pageable) {
        log.info("############ call getAllConsumers ############");
        Page<ConsumerProfile> consumerProfilePage = getAllConsumersUseCase.handle(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending()));

        List<GetAllConsumerUsersResponse> getAllConsumerUsersResponseList = consumerProfilePage.getContent().stream()
                .map(cp -> GetAllConsumerUsersResponse.getAllConsumerResponseBuilder().userId(cp.getUser().getId()).email(cp.getUser().getEmail()).firstName(cp.getFirstName())
                        .lastName(cp.getLastName()).avatar(ImageUtils.getProfileImageURLForConsumerUser(cp)).gender(cp.getGender().getGenderName()).phone(cp.getPhone())
                        .state(cp.getZipCode().getState().getName()).zipCode(cp.getZipCode().getCode().toString()).userType(cp.getUserType().getPrettyName()).isActive(cp.getEnabled())
                        .verified(cp.getVerified()).build()).collect(Collectors.toList());
        return new PageResponseImpl<>(getAllConsumerUsersResponseList, pageable, consumerProfilePage.getTotalElements());
    }

    @Operation(summary = "Get all Dispensaries")
    @PageableAsQueryParam
    @GetMapping(value = Routes.GET_ALL_PROFILE_DISPENSARY_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PageResponse<GetAllDispensaryUsersResponse> getAllDispensaries(@Parameter(hidden = true, required = true) PageableRequest pageable) {
        log.info("############ call getAllDispensaries ############");
        Page<DispensaryProfile> dispensaryProfilePage = getAllDispensariesUseCase.handle(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending()));
        List<GetAllDispensaryUsersResponse> getAllDispensaryUsersResponseList = dispensaryProfilePage.getContent().stream()
                .map(dispensaryProfile -> modelMapper.map(dispensaryProfile, GetAllDispensaryUsersResponse.class))
                .collect(Collectors.toList());
        return new PageResponseImpl<>(getAllDispensaryUsersResponseList, pageable, dispensaryProfilePage.getTotalElements());
    }

    @Operation(summary = "Update Admin by user id")
    @PutMapping(value = Routes.UPDATE_ADMIN_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public UpdateAdminProfileResponse updateAdmin(
            @Parameter(name = "userId", required = true, description = "identifier associated with the user (1..N)")
            @Min(value = 1, message = "userId should be greater than zero")
            @PathVariable(name = "userId") Long userId,
            @RequestBody @Valid UpdateAdminProfileRequest request) throws NotFoundException {
        log.info("############ call updateAdmin [{}] ############", userId);
        AdminProfile adminProfile = modelMapper.map(request, AdminProfile.class);
        try {
            return modelMapper.map(updateAdminUseCase.handle(userId, adminProfile), UpdateAdminProfileResponse.class);
        } catch (ProfileUserException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Operation(summary = "Update Consumer by user id")
    @PutMapping(value = Routes.UPDATE_CONSUMER_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public UpdateConsumerProfileResponse updateConsumer(@Parameter(name = "userId", required = true, description = "identifier associated with the user (0..N)") @Min(value = 0, message = "userId should be greater than " + "zero")
                                                        @PathVariable(name = "userId") Long userId,
                                                        @RequestBody @Valid UpdateConsumerProfileRequest request) throws NotFoundException {
        log.info("############ call updateConsumer [{}] ############", userId);
        ConsumerProfile consumerProfile = modelMapper.map(request, ConsumerProfile.class);
        consumerProfile.setProfileImage(new ProfileImage(null, request.getAvatar(), ResourceStatusEnum.PENDING, LocalDateTime.now()));

        try {
            ConsumerProfile profile = updateConsumerUseCase.handle(userId, consumerProfile);
            UpdateConsumerProfileResponse response = modelMapper.map(profile, UpdateConsumerProfileResponse.class);
            response.setAvatar(profile.getProfileImage() != null ? profile.getProfileImage().getImageURL() : null);

            return response;
        } catch (ProfileUserException | GenderNotFoundException | StateNotFoundException |
                 ZipcodeNotFoundDomainException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Operation(summary = "Update Dispensary by user id")
    @PutMapping(value = Routes.UPDATE_DISPENSARY_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public UpdateDispensaryProfileResponse updateDispensary(@Parameter(name = "userId", required = true, description = "identifier associated with the user (0..N)") @Min(value = 0, message = "userId should be greater than " + "zero")
                                                            @PathVariable(name = "userId") Long userId,
                                                            @RequestBody @Valid UpdateDispensaryProfileRequest request) throws NotFoundException {
        log.info("############ call updateDispensary [{}] ############", userId);
        DispensaryProfile dispensaryProfile = modelMapper.map(request, DispensaryProfile.class);
        try {
            return modelMapper.map(updateDispensaryUseCase.handle(userId, dispensaryProfile), UpdateDispensaryProfileResponse.class);
        } catch (ProfileUserException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    @Operation(summary = "Remove a user")
    @DeleteMapping(value = Routes.DELETE_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public DeleteUserResponse deleteUserById(@PathVariable("userId") Long userId) throws NotFoundException {
        log.info("############ call deleteUserById [{}] ############", userId);
        try {
            this.deleteUserUseCase.handle(userId);
        } catch (UserNotFoundException e) {
            throw new NotFoundException();
        }
        return DeleteUserResponse.builder()
                .id(userId)
                .build();
    }

    @Override
    @Operation(summary = "Remove admin profile by user id")
    @DeleteMapping(value = Routes.DELETE_ADMIN_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public DeleteAdminProfileResponse deleteAdminUserById(@Parameter(name = "userId", required = true, description = "identifier associated with the user (0..N)")
                                                          @Min(value = 0, message = "userId should be greater than zero")
                                                          @PathVariable("userId") Long userId) throws NotFoundException {
        log.info("############ call deleteAdminUserById [{}] ############", userId);
        try {
            AdminProfile profile = this.deleteAdminUserUseCase.handle(userId);
            return modelMapper.map(profile, DeleteAdminProfileResponse.class);
        } catch (UserNotFoundException | NotFoundProfileException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    @Operation(summary = "Remove consumer profile by user id")
    @DeleteMapping(value = Routes.DELETE_CONSUMER_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public DeleteConsumerProfileResponse deleteConsumerUserById(@Parameter(name = "userId", required = true, description = "identifier associated with the user (0..N)")
                                                                @Min(value = 0, message = "userId should be greater than zero")
                                                                @PathVariable("userId") Long userId) throws NotFoundException {
        log.info("############ call deleteConsumerUserById [{}] ############", userId);
        try {
            ConsumerProfile profile = this.deleteConsumerUserUseCase.handle(userId);
            return modelMapper.map(profile, DeleteConsumerProfileResponse.class);
        } catch (UserNotFoundException | NotFoundProfileException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    @Operation(summary = "Remove dispensary profile by dispensaryId")
    @DeleteMapping(value = Routes.DELETE_DISPENSARY_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public DeleteDispensaryProfileResponse deleteDispensaryUserByDispensaryId(@Parameter(name = "dispensaryId", required = true, description = "identifier associated with the dispensary profile (1..N)")
                                                                    @Min(value = 1, message = "dispensaryId should be greater than zero")
                                                                    @PathVariable("dispensaryId") Long dispensaryId) throws NotFoundException {
        log.info("############ call deleteDispensaryUserByDispensaryId [{}] ############", dispensaryId);
        try {
            DispensaryProfile profile = this.deleteDispensaryUserUseCase.handle(dispensaryId);
            return modelMapper.map(profile, DeleteDispensaryProfileResponse.class);
        } catch (UserNotFoundException | NotFoundProfileException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    @Operation(summary = "Activate an Admin profile")
    @PatchMapping(value = Routes.ACTIVATE_ADMIN_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ActiveStatusProfileResponse activateAdmin(@Parameter(name = "userId", required = true, description = "identifier associated with the user (0..N)")
                                                        @Min(value = 1, message = "userId should be greater than zero")
                                                        @PathVariable(name = "userId") Long userId) throws ProfileException {
        log.info("############ call activeAdmin [{}] ############", userId);
        try {
            Profile profile = enableAdminUseCase.handle(userId);
            return ActiveStatusProfileResponse.builder()
                    .id(profile.getUser().getId())
                    .enabled(profile.getEnabled())
                    .build();
        } catch (NotFoundProfileException e) {
            throw new ProfileNotFoundException(e.getMessage());
        } catch (TypeNotFoundProfileException e) {
            throw new ProfileTypeNotFoundException(e.getMessage());
        } catch (BadRequestProfileException | EnableStatusProfileException e) {
            throw new ProfileException(e.getMessage());
        }
    }

    @Override
    @Operation(summary = "Deactivate an Admin profile")
    @PatchMapping(value = Routes.DEACTIVATE_ADMIN_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ActiveStatusProfileResponse deactivateAdmin(@Parameter(name = "userId", required = true, description = "identifier associated with the user (0..N)")
                                                            @Min(value = 1, message = "userId should be greater than zero")
                                                            @PathVariable(name = "userId") Long userId) throws ProfileException {
        log.info("############ call deactivateAdmin [{}] ############", userId);
        try {
            Profile profile = disableAdminUseCase.handle(userId);
            return ActiveStatusProfileResponse.builder()
                    .id(profile.getUser().getId())
                    .enabled(profile.getEnabled())
                    .build();
        } catch (NotFoundProfileException e) {
            throw new ProfileNotFoundException(e.getMessage());
        } catch (TypeNotFoundProfileException e) {
            throw new ProfileTypeNotFoundException(e.getMessage());
        } catch (BadRequestProfileException | EnableStatusProfileException e) {
            throw new ProfileException(e.getMessage());
        }
    }

    @Override
    @Operation(summary = "Activate a ConsumerUser profile")
    @PatchMapping(value = Routes.ACTIVATE_CONSUMER_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ActiveStatusProfileResponse activateConsumerUser(
            @Parameter(name = "userId", required = true, description = "identifier associated with the user (1..N)")
            @Min(value = 1, message = "userId should be greater than zero")
            @PathVariable(name = "userId") Long userId)
            throws ProfileNotFoundException, ProfileEnableStatusExceptionResponseApi {
        log.info("############ call activateConsumerUser [{}] ############", userId);
        try {
            return modelMapper
                    .map(toggleEnabledProfileUseCaseImpl.handle(userId, ProfileTypeEnum.CONSUMER, Boolean.TRUE),
                    ActiveStatusProfileResponse.class);
        } catch (NotFoundProfileException e) {
            throw new ProfileNotFoundException(e.getMessage());
        } catch (EnableStatusProfileException e) {
            throw new ProfileEnableStatusExceptionResponseApi(e.getMessage());
        }
    }

    @Override
    @Operation(summary = "Deactivate a ConsumerUser profile")
    @PatchMapping(value = Routes.DEACTIVATE_CONSUMER_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ActiveStatusProfileResponse deactivateConsumerUser(
            @Parameter(name = "userId", required = true, description = "identifier associated with the user (1..N)")
            @Min(value = 1, message = "userId should be greater than zero")
            @PathVariable(name = "userId") Long userId)
            throws ProfileNotFoundException, ProfileEnableStatusExceptionResponseApi {
        log.info("############ call deactivateConsumerUser [{}] ############", userId);
        try {
            return modelMapper
                    .map(toggleEnabledProfileUseCaseImpl.handle(userId, ProfileTypeEnum.CONSUMER, Boolean.FALSE),
                            ActiveStatusProfileResponse.class);
        } catch (NotFoundProfileException e) {
            throw new ProfileNotFoundException(e.getMessage());
        } catch (EnableStatusProfileException e) {
            throw new ProfileEnableStatusExceptionResponseApi(e.getMessage());
        }
    }

    @Override
    @Operation(summary = "Activate a dispensary profile")
    @PatchMapping(value = Routes.ACTIVATE_DISPENSARY_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ActiveStatusProfileResponse activateDispensaryUser(
            @Parameter(name = "userId", required = true, description = "identifier associated with the user (1..N)")
            @Min(value = 1, message = "userId should be greater than zero")
            @PathVariable(name = "userId") Long userId)
            throws ProfileNotFoundException, ProfileEnableStatusExceptionResponseApi {
        log.info("############ call activateConsumerUser [{}] ############", userId);
        try {
            return modelMapper
                    .map(toggleEnabledProfileUseCaseImpl.handle(userId, ProfileTypeEnum.DISPENSARY, Boolean.TRUE),
                            ActiveStatusProfileResponse.class);
        } catch (NotFoundProfileException e) {
            throw new ProfileNotFoundException(e.getMessage());
        } catch (EnableStatusProfileException e) {
            throw new ProfileEnableStatusExceptionResponseApi(e.getMessage());
        }
    }

    @Override
    @Operation(summary = "Deactivate a dispensary profile")
    @PatchMapping(value = Routes.DEACTIVATE_DISPENSARY_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ActiveStatusProfileResponse deactivateDispensaryUser(
            @Parameter(name = "userId", required = true, description = "identifier associated with the user (1..N)")
            @Min(value = 1, message = "userId should be greater than zero")
            @PathVariable(name = "userId") Long userId)
            throws ProfileNotFoundException, ProfileEnableStatusExceptionResponseApi {
        log.info("############ call activateConsumerUser [{}] ############", userId);
        try {
            return modelMapper
                    .map(toggleEnabledProfileUseCaseImpl.handle(userId, ProfileTypeEnum.DISPENSARY, Boolean.FALSE),
                            ActiveStatusProfileResponse.class);
        } catch (NotFoundProfileException e) {
            throw new ProfileNotFoundException(e.getMessage());
        } catch (EnableStatusProfileException e) {
            throw new ProfileEnableStatusExceptionResponseApi(e.getMessage());
        }
    }

    private ValidateProfileResponse createValidateProfileResponse(Profile profile) {
        if (profile == null) {
            return null;
        }
        if (DispensaryProfile.class.isAssignableFrom(profile.getClass())) {
            DispensaryProfile dp = (DispensaryProfile) profile;
            return new ValidateProfileResponse(profile.getId(), profile.getProfileType().getProfile().name(), dp.getEnabled(), dp.getDispensaryId());
        }
        return new ValidateProfileResponse(profile.getId(), profile.getProfileType().getProfile().name(), profile.getEnabled());
    }

    @Override
    @Operation(summary = "Check if email and password are valid")
    @PostMapping(value = Routes.VALIDATE_USER, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ValidateUserResponse validateUser(@Parameter(description = "Credentials", required = true) @RequestBody @Valid ValidateUserRequest request) throws NotFoundException, UnauthorizedException {
        log.info("############ call validateUser [{}] ############", request.getEmail());
        User user;
        try {
            user = validateUserUseCase.handle(request.getEmail(), request.getPassword());
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (PasswordInvalidException e) {
            throw new UnauthorizedException(e.getMessage());
        }
        return ValidateUserResponse.builder()
                .userId(user.getId())
                .profiles(
                        user.getProfiles() != null ?
                                user.getProfiles().stream()
                                        .map(this::createValidateProfileResponse)
                                        .collect(Collectors.toList())
                                : null)
                .build();
    }

    @Override
    @Operation(summary = "Create and send a validation code")
    @PostMapping(value = Routes.SEND_CODE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void sendCode(@RequestBody @Valid SendCodeRequest request) throws NotFoundException, InvalidRequestException {
        log.info("############ call sendCode [{}] ############", request.getEmail());
        try {
            sendCodeUseCase.handle(request.getEmail());
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (InvalidException e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }

    @Override
    @Operation(summary = "Verify if validation code is valid")
    @PostMapping(value = Routes.VERIFY_CODE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void verifyCode(@RequestBody @Valid VerifyCodeRequest request) throws NotFoundException, InvalidRequestException {
        log.info("############ call verifyCode [{}] ############", request.getEmail());
        try {
            verifyCodeUseCase.handle(request.getEmail(), request.getCode());
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (InvalidException e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }

    @Operation(summary = "Search users")
    @PostMapping(value = Routes.SEARCH_USERS, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<Object> searchUsers() {
        log.info("############ call searchUsers ############");
        return null;
    }

    @Operation(summary = "Get all genders")
    @PageableAsQueryParam
    @GetMapping(value = Routes.GET_ALL_GENDERS, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PageResponse<GenderResponse> getAllGenders(@Parameter(hidden = true, required = true) PageableRequest pageable) {
        log.debug("############ call getAllGenders ############");
        Page<Gender> genders = this.getAllGendersUseCase.handle(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending()));
        List<GenderResponse> response = genders.getContent().stream()
                .map(b -> this.modelMapper.map(b, GenderResponse.class))
                .collect(Collectors.toList());
        return new PageResponseImpl<>(response, pageable, genders.getTotalElements());
    }

    @Operation(summary = "Get gender by id")
    @GetMapping(value = Routes.GET_GENDER_BY_ID, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public GenderResponse getGenderById(@PathVariable("genderId") long genderId) throws io.oigres.ecomm.service.users.api.model.exception.GenderNotFoundException {
        log.debug("############ call getGenderById ############");
        Gender gender;
        try {
            gender = getGenderByIdUseCase.handle(genderId);
        } catch (io.oigres.ecomm.service.users.exception.NotFoundException e) {
            throw new io.oigres.ecomm.service.users.api.model.exception.GenderNotFoundException();
        }
        return GenderResponse.builder().id(gender.getId()).genderName(gender.getGenderName()).build();
    }

    @Operation(summary = "Get all user types")
    @PageableAsQueryParam
    @GetMapping(value = Routes.GET_ALL_USERTYPES, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PageResponse<UserTypeResponse> getAllUserTypes(@Parameter(hidden = true, required = true) PageableRequest pageable) {
        log.debug("############ call getAllUserTypes ############");
        List<UserTypeResponse> response = Arrays.stream(ConsumerTypeEnum.values()).map(ct -> UserTypeResponse.builder().id(ct.getId()).name(ct.getPrettyName()).build()).collect(Collectors.toList());
        return new PageResponseImpl<>(response, pageable, response.size());
    }

    @Operation(summary = "Get user type by id")
    @GetMapping(value = Routes.GET_USERTYPE_BY_ID, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public UserTypeResponse getUserTypeById(@PathVariable("userTypeId") long userTypeId) throws UserTypeNotFoundException {
        log.debug("############ call getUserTypeById ############");
        ConsumerTypeEnum userType = ConsumerTypeEnum.getById(userTypeId).orElseThrow(UserTypeNotFoundException::new);
        return UserTypeResponse.builder().id(userType.getId()).name(userType.getPrettyName()).build();
    }

    @Operation(summary = "Retrieve state tax for a user")
    @GetMapping(value = Routes.GET_USER_STATE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GetConsumerStateTax getStateTaxByUserId(@PathVariable("userId") Long userId) throws NotFoundException {
        log.info("############ call getStateTaxByUserId [{}] ############", userId);
        try {
            ConsumerProfile consumerProfile = getConsumerByIdUseCase.handle(userId);
            return GetConsumerStateTax.builder().tax(consumerProfile.getZipCode().getTax()).build();
        } catch (NotFoundProfileException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    @Operation(summary = "Change profile image status to UPLOADED")
    @PatchMapping(value = Routes.EDIT_PROFILE_IMAGE_STATUS, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UpdateProfileImageResponse changeProfileImageStatus(@RequestParam String imageUrl) {
        log.info("############ call changeProfileImageStatus [{}] ############", imageUrl);
        UpdateProfileImageDTO response = this.changeProfileImageStatusUseCase.handle(imageUrl);

        return UpdateProfileImageResponse.builder()
                .id(response.getId())
                .status(response.getStatus())
                .imageURL(response.getImageURL())
                .build();
    }

    @Operation(summary = "Get URL where a user's avatar image should be uploaded")
    @GetMapping(value = Routes.GET_AVATAR_IMAGE_UPLOAD_LOCATION, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public ImageUploadLocationResponse getAvatarImageUploadLocation(
            @Parameter(description = "File extension which will be uploaded", required = true)
            @NotNull @NotBlank @RequestParam("extension") String extension
    ) {
        StoreLocation location = this.getStoreLocationUseCase.handle(BlobType.PROFILE_IMAGE, extension);

        return ImageUploadLocationResponse.builder()
                .uploadURL(location.getUploadURL().toString())
                .httpMethod(location.getHttpMethod())
                .key(location.getKey())
                .build();
    }
    @Operation(summary = "Get URL where a user's MMJCard image should be uploaded")
    @GetMapping(value = Routes.GET_MMJCARD_IMAGE_UPLOAD_LOCATION, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public ImageUploadLocationResponse getMmjCardImageUploadLocation(
            @Parameter(description = "File extension which will be uploaded", required = true)
            @NotNull @NotBlank @RequestParam("extension") String extension) {
        StoreLocation location = this.getStoreLocationUseCase.handle(BlobType.MMJ_CARD_IMAGE, extension);

        return ImageUploadLocationResponse.builder()
                .uploadURL(location.getUploadURL().toString())
                .httpMethod(location.getHttpMethod())
                .key(location.getKey())
                .build();
    }

    @Override
    @Operation(summary = "Change card image status to UPLOADED")
    @PatchMapping(value = Routes.UPLOADS_CARDS, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UpdateCardImageToUploadedStateResponse updateCardImageStatus(@RequestParam String imageUrl) {
        log.info("############ call changeBrandImageStatus [{}] ############", imageUrl);
        return modelMapper.map(changeCardImageStatusUseCase.handle(imageUrl), UpdateCardImageToUploadedStateResponse.class);

    }
}
