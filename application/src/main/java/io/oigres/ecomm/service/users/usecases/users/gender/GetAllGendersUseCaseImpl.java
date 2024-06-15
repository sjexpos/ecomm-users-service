package io.oigres.ecomm.service.users.usecases.users.gender;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.Gender;
import io.oigres.ecomm.service.users.repository.GenderRepository;


@Component
public class GetAllGendersUseCaseImpl implements GetAllGendersUseCase {

    private final GenderRepository genderRepository;

    public GetAllGendersUseCaseImpl(GenderRepository genderRepository) {
        this.genderRepository = genderRepository;
    }

    public Page<Gender> handle(Pageable pageable) {
        return genderRepository.findAll(pageable);
    }
}
