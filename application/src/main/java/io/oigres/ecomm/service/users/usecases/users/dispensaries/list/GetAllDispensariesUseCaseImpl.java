package io.oigres.ecomm.service.users.usecases.users.dispensaries.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.repository.profiles.DispensaryProfileRepository;

@Component
public class GetAllDispensariesUseCaseImpl implements GetAllDispensariesUseCase {
    private DispensaryProfileRepository dispensaryProfileRepository;

    public GetAllDispensariesUseCaseImpl(DispensaryProfileRepository dispensaryProfileRepository) {
        this.dispensaryProfileRepository = dispensaryProfileRepository;
    }

    @Override
    public Page<DispensaryProfile> handle(Pageable pageable) {
        return dispensaryProfileRepository.findAll(pageable);
    }
}
