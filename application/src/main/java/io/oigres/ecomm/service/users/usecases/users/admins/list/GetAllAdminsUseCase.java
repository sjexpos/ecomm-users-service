package io.oigres.ecomm.service.users.usecases.users.admins.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.users.domain.profile.AdminProfile;

public interface GetAllAdminsUseCase {
    Page<AdminProfile> handle(Pageable pageable);
}
