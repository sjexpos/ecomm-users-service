package io.oigres.ecomm.service.users.usecases.uploads;

import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.StoreLocation;
import io.oigres.ecomm.service.users.enums.BlobType;
import io.oigres.ecomm.service.users.repository.BlobRepository;

@Component
public class GetStoreLocationUseCase {

    private BlobRepository blobRepository;

    public GetStoreLocationUseCase(BlobRepository blobRepository) {
        this.blobRepository = blobRepository;
    }

    public StoreLocation handle(BlobType blobType, String extension) {
        return this.blobRepository.getStoreLocation(blobType, extension);
    }

}
