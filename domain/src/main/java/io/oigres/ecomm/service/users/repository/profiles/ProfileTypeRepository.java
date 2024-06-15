package io.oigres.ecomm.service.users.repository.profiles;

import java.util.List;
import java.util.Optional;

import io.oigres.ecomm.service.users.domain.ProfileType;
import io.oigres.ecomm.service.users.enums.ProfileTypeEnum;
import io.oigres.ecomm.service.users.repository.GenericRepository;

public interface ProfileTypeRepository extends GenericRepository<ProfileType, Long> {
    Optional<ProfileType> findByProfile(ProfileTypeEnum profileTypeEnum);

    List<ProfileType> findAll();
}
