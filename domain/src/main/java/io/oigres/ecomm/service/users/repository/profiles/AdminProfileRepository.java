package io.oigres.ecomm.service.users.repository.profiles;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.repository.GenericRepository;

import java.util.Optional;

public interface AdminProfileRepository extends GenericRepository<AdminProfile, Long> {

    Optional<AdminProfile> findById(Long userId);
    
    Page<AdminProfile> findAll(Pageable pageable);
    
    AdminProfile save(AdminProfile adminProfile);

}
