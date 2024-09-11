package io.oigres.ecomm.service.users.config.mapper;

import io.oigres.ecomm.service.users.api.model.admin.*;
import io.oigres.ecomm.service.users.api.model.consumer.*;
import io.oigres.ecomm.service.users.api.model.dispensary.*;
import io.oigres.ecomm.service.users.api.model.profile.ActiveStatusProfileResponse;
import io.oigres.ecomm.service.users.api.txoutbox.UserOutbox;
import io.oigres.ecomm.service.users.domain.Profile;
import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class ModelMapperConfiguration {
    @Bean
    public ModelMapper customModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setAmbiguityIgnored(true);

        TypeMap<AdminProfile, CreateAdminUserResponse> typeMapAdminResponse = modelMapper.createTypeMap(AdminProfile.class, CreateAdminUserResponse.class);
        typeMapAdminResponse.addMappings( mapper -> {
            mapper.map( source -> source.getUser().getEmail(), CreateAdminUserResponse::setEmail);
            mapper.map( source -> source.getUser().getId(), CreateAdminUserResponse::setId);
            mapper.map(Profile::getEnabled, CreateAdminUserResponse::setIsActive);
        });

        TypeMap<UpdateAdminProfileRequest, AdminProfile> typeMapUpdateAdminRequest = modelMapper.createTypeMap(UpdateAdminProfileRequest.class, AdminProfile.class);
        typeMapUpdateAdminRequest.addMappings( mapper -> {
            mapper.<String>map( UpdateAdminProfileRequest::getPassword, (destination, value) -> destination.getUser().setPassword(value));
            mapper.<String>map( UpdateAdminProfileRequest::getEmail, (destination, value) -> destination.getUser().setEmail(value));
        });

        TypeMap<AdminProfile, UpdateAdminProfileResponse> typeMapUpdateAdminResponse = modelMapper.createTypeMap(AdminProfile.class, UpdateAdminProfileResponse.class);
        typeMapUpdateAdminResponse.addMappings( mapper -> {
            mapper.map( source -> source.getUser().getId(), UpdateAdminProfileResponse::setId);
            mapper.map( source -> source.getUser().getEmail(), UpdateAdminProfileResponse::setEmail);
            mapper.map( AdminProfile::getEnabled, UpdateAdminProfileResponse::setIsActive);
        });

        TypeMap<UpdateConsumerProfileRequest, ConsumerProfile> typeMapUpdateConsumerRequest = modelMapper.createTypeMap(UpdateConsumerProfileRequest.class, ConsumerProfile.class);
        typeMapUpdateConsumerRequest.addMappings( mapper -> {
            mapper.skip(ConsumerProfile::setId);
            mapper.<String>map( UpdateConsumerProfileRequest::getPassword, (destination, value) -> destination.getUser().setPassword(value));
            mapper.<Long>map( UpdateConsumerProfileRequest::getGenderId, (destination, value) -> destination.getGender().setId(value));
            mapper.<Long>map( UpdateConsumerProfileRequest::getZipcodeStateId, (destination, value) -> destination.getZipCode().getState().setId(value));
            mapper.<Long>map( UpdateConsumerProfileRequest::getZipcodeId, (destination, value) -> destination.getZipCode().setId(value));
            mapper.<String>map( UpdateConsumerProfileRequest::getAvatar, (destination, value) -> destination.getProfileImage().setImageURL(value));
        });

        TypeMap<ConsumerProfile, UpdateConsumerProfileResponse> typeMapUpdateConsumerResponse = modelMapper.createTypeMap(ConsumerProfile.class, UpdateConsumerProfileResponse.class);
        typeMapUpdateConsumerResponse.addMappings( mapper -> {
            mapper.map( source -> source.getUser().getId(), UpdateConsumerProfileResponse::setId);
            mapper.map( source -> source.getUser().getEmail(), UpdateConsumerProfileResponse::setEmail);
            mapper.map( ConsumerProfile::getEnabled, UpdateConsumerProfileResponse::setIsActive);
            mapper.map( source -> source.getGender().getId(), UpdateConsumerProfileResponse::setGenderId);
            mapper.map( source -> source.getZipCode().getState().getId(), UpdateConsumerProfileResponse::setZipcodeStateId);
            mapper.map( source -> source.getZipCode().getId(), UpdateConsumerProfileResponse::setZipcodeId);
            mapper.map( source -> source.getCard().getCardImage().getImageURL(), UpdateConsumerProfileResponse::setMmjCard);
            mapper.map( source -> source.getProfileImage().getImageURL(), UpdateConsumerProfileResponse::setAvatar);

        });

        TypeMap<UpdateDispensaryProfileRequest, DispensaryProfile> typeMapUpdateDispensaryRequest = modelMapper.createTypeMap(UpdateDispensaryProfileRequest.class, DispensaryProfile.class);
        typeMapUpdateDispensaryRequest.addMappings( mapper -> {
            mapper.<String>map( UpdateDispensaryProfileRequest::getPassword, (destination, value) -> destination.getUser().setPassword(value));
        });

        TypeMap<DispensaryProfile, UpdateDispensaryProfileResponse> typeMapUpdateDispensaryResponse = modelMapper.createTypeMap(DispensaryProfile.class, UpdateDispensaryProfileResponse.class);
        typeMapUpdateDispensaryResponse.addMappings( mapper -> {
            mapper.map( source -> source.getUser().getId(), UpdateDispensaryProfileResponse::setId);
            mapper.map( source -> source.getUser().getEmail(), UpdateDispensaryProfileResponse::setEmail);
            mapper.map( DispensaryProfile::getEnabled, UpdateDispensaryProfileResponse::setIsActive);
        });

        TypeMap<AdminProfile, GetAdminUserResponse> typeMapGetAdminResponse = modelMapper.createTypeMap(AdminProfile.class, GetAdminUserResponse.class);
        typeMapGetAdminResponse.addMappings( mapper -> {
            mapper.map( source -> source.getUser().getEmail(), GetAdminUserResponse::setEmail);
            mapper.map(Profile::getEnabled, GetAdminUserResponse::setIsActive);
        });

        TypeMap<AdminProfile, GetAllAdminUsersResponse> typeMapGetAllAdminResponse = modelMapper.createTypeMap(AdminProfile.class, GetAllAdminUsersResponse.class);
        typeMapGetAllAdminResponse.addMappings( mapper -> {
            mapper.map( source -> source.getUser().getEmail(), GetAllAdminUsersResponse::setEmail);
            mapper.map(Profile::getEnabled, GetAllAdminUsersResponse::setIsActive);
        });

        TypeMap<AdminProfile, DeleteAdminProfileResponse> typeMapDeleteAdminResponse = modelMapper.createTypeMap(AdminProfile.class, DeleteAdminProfileResponse.class);
        typeMapDeleteAdminResponse.addMappings( mapper -> {
            mapper.map( source -> source.getUser().getId(), DeleteAdminProfileResponse::setId);
            mapper.map( source -> source.getUser().getEmail(), DeleteAdminProfileResponse::setEmail);
            mapper.map( AdminProfile::getEnabled, DeleteAdminProfileResponse::setIsActive);
        });

        TypeMap<CreateDispensaryUserRequest, DispensaryProfile> typeMapDispensaryRequest = modelMapper.createTypeMap(CreateDispensaryUserRequest.class, DispensaryProfile.class);
        typeMapDispensaryRequest.addMappings( mapper -> {
            mapper.skip(DispensaryProfile::setId);
            mapper.map(CreateDispensaryUserRequest::getDispensaryId, DispensaryProfile::setDispensaryId);
        });

        TypeMap<DispensaryProfile, CreateDispensaryUserResponse> typeMapDispensaryResponse = modelMapper.createTypeMap(DispensaryProfile.class, CreateDispensaryUserResponse.class);
        typeMapDispensaryResponse.addMappings( mapper -> {
            mapper.map( source -> source.getUser().getEmail(), CreateDispensaryUserResponse::setEmail);
            mapper.map( source -> source.getUser().getId(), CreateDispensaryUserResponse::setId);
            mapper.map(Profile::getEnabled, CreateDispensaryUserResponse::setIsActive);
        });

        TypeMap<DispensaryProfile, GetDispensaryUserResponse> typeMapGetDispensaryResponse = modelMapper.createTypeMap(DispensaryProfile.class, GetDispensaryUserResponse.class);
        typeMapGetDispensaryResponse.addMappings( mapper -> {
            mapper.map( source -> source.getUser().getEmail(), GetDispensaryUserResponse::setEmail);
            mapper.map(Profile::getEnabled, GetDispensaryUserResponse::setIsActive);
        });

        TypeMap<DispensaryProfile, GetAllDispensaryUsersResponse> typeMapAllGetDispensaryResponse = modelMapper.createTypeMap(DispensaryProfile.class, GetAllDispensaryUsersResponse.class);
        typeMapAllGetDispensaryResponse.addMappings( mapper -> {
            mapper.map( source -> source.getUser().getEmail(), GetAllDispensaryUsersResponse::setEmail);
            mapper.map(Profile::getEnabled, GetAllDispensaryUsersResponse::setIsActive);
        });

        TypeMap<DispensaryProfile, DeleteDispensaryProfileResponse> typeMapDeleteDispensaryResponse = modelMapper.createTypeMap(DispensaryProfile.class, DeleteDispensaryProfileResponse.class);
        typeMapDeleteDispensaryResponse.addMappings( mapper -> {
            mapper.map( source -> source.getUser().getId(), DeleteDispensaryProfileResponse::setId);
            mapper.map( source -> source.getUser().getEmail(), DeleteDispensaryProfileResponse::setEmail);
            mapper.map( DispensaryProfile::getEnabled, DeleteDispensaryProfileResponse::setIsActive);
        });

        TypeMap<CreateConsumerUserRequest, ConsumerProfile> typeMapConsumerRequest = modelMapper.createTypeMap(CreateConsumerUserRequest.class, ConsumerProfile.class);
        typeMapConsumerRequest.addMappings( mapper -> {
            mapper.skip(ConsumerProfile::setId);
            mapper.skip((destination, value) -> destination.getCard().getId());
        });

        TypeMap<ConsumerProfile, CreateConsumerUserResponse> typeMapConsumerResponse = modelMapper.createTypeMap(ConsumerProfile.class, CreateConsumerUserResponse.class);
        typeMapConsumerResponse.addMappings( mapper -> {
            mapper.map( source -> source.getUser().getEmail(), CreateConsumerUserResponse::setEmail);
            mapper.map( source -> source.getUser().getId(), CreateConsumerUserResponse::setId);
            mapper.map(Profile::getEnabled, CreateConsumerUserResponse::setIsActive);
            mapper.map( source -> source.getGender().getId(), CreateConsumerUserResponse::setGenderId);
            mapper.map( source -> source.getZipCode().getState().getId(), CreateConsumerUserResponse::setZipcodeStateId);
            mapper.map( source -> source.getZipCode().getId(), CreateConsumerUserResponse::setZipcodeId);
            mapper.map( source -> source.getCard().getCardImage().getImageURL(), CreateConsumerUserResponse::setCardImageURL);
            mapper.map( source -> source.getProfileImage().getImageURL(), CreateConsumerUserResponse::setAvatar);
        });

        TypeMap<ConsumerProfile, GetConsumerUserResponse> typeMapGetConsumerResponse = modelMapper.createTypeMap(ConsumerProfile.class, GetConsumerUserResponse.class);
        typeMapGetConsumerResponse.addMappings( mapper -> {
            mapper.map( source -> source.getUser().getEmail(), GetConsumerUserResponse::setEmail);
            mapper.map( source -> source.getUser().getId(), GetConsumerUserResponse::setUserId);
            mapper.map(Profile::getEnabled, GetConsumerUserResponse::setIsActive);
            mapper.map( source -> source.getGender().getId(), GetConsumerUserResponse::setGenderId);
            mapper.map(source -> source.getCard().getCardImage().getImageURL(), GetConsumerUserResponse::setMmjCard);
            mapper.map( source -> source.getZipCode().getState().getId(), GetConsumerUserResponse::setZipcodeStateId);
            mapper.map( source -> source.getZipCode().getId(), GetConsumerUserResponse::setZipcodeId);
        });

        TypeMap<ConsumerProfile, DeleteConsumerProfileResponse> typeMapDeleteConsumerResponse = modelMapper.createTypeMap(ConsumerProfile.class, DeleteConsumerProfileResponse.class);
        typeMapDeleteConsumerResponse.addMappings( mapper -> {
            mapper.map( source -> source.getUser().getId(), DeleteConsumerProfileResponse::setId);
            mapper.map( source -> source.getUser().getEmail(), DeleteConsumerProfileResponse::setEmail);
            mapper.map( ConsumerProfile::getEnabled, DeleteConsumerProfileResponse::setIsActive);
            mapper.map( source -> source.getZipCode().getState().getName(), DeleteConsumerProfileResponse::setState);
            mapper.map( source -> source.getZipCode().getCode(), DeleteConsumerProfileResponse::setZipCode);
        });

        TypeMap<Profile, ActiveStatusProfileResponse> typeMapActiveStatusProfileResponse = modelMapper.createTypeMap(Profile.class, ActiveStatusProfileResponse.class);
        typeMapActiveStatusProfileResponse.addMappings( mapper -> {
            mapper.map( source -> source.getUser().getId(), ActiveStatusProfileResponse::setId);
            mapper.map(Profile::getEnabled, ActiveStatusProfileResponse::setEnabled);
        });

        TypeMap<User, UserOutbox> typeMapUserOutbox = modelMapper.createTypeMap(User.class, UserOutbox.class);
        typeMapUserOutbox.addMappings( mapper -> {
            mapper.map(User::getId, UserOutbox::setId);
            mapper.map(User::getEmail, UserOutbox::setEmail);
            mapper.map(User::getCreatedAt, UserOutbox::setCreatedAt);
            mapper.map(User::getModifiedAt, UserOutbox::setModifiedAt);
            mapper.map(User::getDeletedAt, UserOutbox::setDeletedAt);
        });
        return modelMapper;
    }

}
