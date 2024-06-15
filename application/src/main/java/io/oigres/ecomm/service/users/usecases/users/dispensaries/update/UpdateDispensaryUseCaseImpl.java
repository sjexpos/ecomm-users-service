package io.oigres.ecomm.service.users.usecases.users.dispensaries.update;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.exception.profile.ProfileUserException;
import io.oigres.ecomm.service.users.repository.profiles.DispensaryProfileRepository;

@Component
public class UpdateDispensaryUseCaseImpl implements UpdateDispensaryUseCase{
    private final DispensaryProfileRepository dispensaryProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public UpdateDispensaryUseCaseImpl(DispensaryProfileRepository dispensaryProfileRepository, PasswordEncoder passwordEncoder) {
        this.dispensaryProfileRepository = dispensaryProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public DispensaryProfile handle(Long userId, DispensaryProfile request) throws ProfileUserException {
        DispensaryProfile current = dispensaryProfileRepository.findById(userId)
                .orElseThrow(NotFoundProfileException::new);
        if( request.getUser() != null && request.getUser().getPassword() != null &&
                !passwordEncoder.matches(request.getUser().getPassword(), current.getUser().getPassword())) {
            current.getUser().setPassword(passwordEncoder.encode(request.getUser().getPassword()));
        }
        if (request.getEnabled() != null) {
            current.setEnabled(request.getEnabled());
        }
        updateDispensary(request, current);
        return dispensaryProfileRepository.save(current);
    }

    private void updateDispensary(DispensaryProfile request, DispensaryProfile current) {
        current.setDispensaryId(request.getDispensaryId());
    }
}
