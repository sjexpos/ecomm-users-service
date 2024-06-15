package io.oigres.ecomm.service.users.usecases.users.gender;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.Gender;
import io.oigres.ecomm.service.users.exception.NotFoundException;
import io.oigres.ecomm.service.users.repository.GenderRepository;

@Component
public class GetGenderByIdUseCaseImpl implements GetGenderByIdUseCase {

    private final GenderRepository genderRepository;

    public GetGenderByIdUseCaseImpl(GenderRepository genderRepository) {
        this.genderRepository = genderRepository;
    }

    public Gender handle(long genderId) throws NotFoundException {
        return genderRepository.findById(genderId).orElseThrow(() -> new NotFoundException("Gender not found"));
    }
}
