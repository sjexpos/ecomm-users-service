package io.oigres.ecomm.service.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.users.domain.ProfileImage;
import io.oigres.ecomm.service.users.repository.ProfileImageRepository;

@Repository
public interface ProfileImageJpaRepository extends JpaRepository<ProfileImage, Long>, ProfileImageRepository {

}