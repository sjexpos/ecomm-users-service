package io.oigres.ecomm.service.users.usecases.users.gender;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.users.domain.Gender;


public interface GetAllGendersUseCase {
    
    Page<Gender> handle(Pageable pageable);

}
