package io.oigres.ecomm.service.users.usecases.users.dispensaries.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.oigres.ecomm.service.users.domain.profile.DispensaryProfile;

public interface GetAllDispensariesUseCase {
    Page<DispensaryProfile> handle(Pageable pageable);
}
