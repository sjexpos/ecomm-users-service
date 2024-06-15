package io.oigres.ecomm.service.users.usecases.users.admins.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.repository.profiles.AdminProfileRepository;

@Component
public class GetAllAdminsUseCaseImpl implements GetAllAdminsUseCase {
    private AdminProfileRepository adminProfileRepository;

    public GetAllAdminsUseCaseImpl(AdminProfileRepository adminProfileRepository) {
        this.adminProfileRepository = adminProfileRepository;
    }

    @Override
    public Page<AdminProfile> handle(Pageable pageable) {
        return adminProfileRepository.findAll(pageable);
    }
}
