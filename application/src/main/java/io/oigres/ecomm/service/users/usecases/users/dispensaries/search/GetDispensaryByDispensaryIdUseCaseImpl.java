package io.oigres.ecomm.service.users.usecases.users.dispensaries.search;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;
import io.oigres.ecomm.service.users.repository.profiles.DispensaryProfileRepository;

@Component
public class GetDispensaryByDispensaryIdUseCaseImpl implements GetDispensaryByDispensaryIdUseCase {
    private DispensaryProfileRepository dispensaryProfileRepository;

    public GetDispensaryByDispensaryIdUseCaseImpl(DispensaryProfileRepository dispensaryProfileRepository) {
        this.dispensaryProfileRepository = dispensaryProfileRepository;
    }

    @Override
    public DispensaryProfile handle(Long dispensaryId) throws NotFoundProfileException {
        return dispensaryProfileRepository.findByDispensaryId(dispensaryId)
                .orElseThrow(NotFoundProfileException::new);
    }
}
