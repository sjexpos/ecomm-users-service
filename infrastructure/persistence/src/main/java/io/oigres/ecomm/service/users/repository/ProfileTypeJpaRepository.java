package io.oigres.ecomm.service.users.repository;

import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.users.domain.ProfileType;
import io.oigres.ecomm.service.users.repository.profiles.ProfileTypeRepository;

@Repository
public interface ProfileTypeJpaRepository extends SearchRepository<ProfileType,Long>, ProfileTypeRepository {
}
