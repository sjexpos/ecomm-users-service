package io.oigres.ecomm.service.users.usecases.users.admins.update;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.repository.profiles.AdminProfileRepository;

@Component
public class UpdateAdminUseCaseImpl implements UpdateAdminUseCase {
    private final AdminProfileRepository adminProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public UpdateAdminUseCaseImpl(AdminProfileRepository adminProfileRepository, PasswordEncoder passwordEncoder) {
        this.adminProfileRepository = adminProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AdminProfile handle(Long profileId, AdminProfile request) throws NotFoundProfileException {
        AdminProfile current = adminProfileRepository.findById(profileId)
                .orElseThrow(NotFoundProfileException::new);
        if (request.getUser() != null && request.getUser().getPassword() != null &&
                !passwordEncoder.matches(request.getUser().getPassword(), current.getUser().getPassword())) {
            current.getUser().setPassword(passwordEncoder.encode(request.getUser().getPassword()));
        }
        if (request.getUser().getEmail() != null) {
            current.getUser().setEmail(request.getUser().getEmail());
        }
        if (request.getEnabled() != null) {
            current.setEnabled(request.getEnabled());
        }
        updateAdmin(request, current);
        return adminProfileRepository.save(current);
    }

    private void updateAdmin(AdminProfile request, AdminProfile current) {
        current.setFirstName(request.getFirstName());
        current.setLastName(request.getLastName());
        current.setPhone(request.getPhone());
    }
}
