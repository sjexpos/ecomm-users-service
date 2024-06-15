package io.oigres.ecomm.service.users.repository;

import java.util.List;

import io.oigres.ecomm.service.users.domain.StoreLocation;
import io.oigres.ecomm.service.users.enums.BlobType;

public interface BlobRepository {
    StoreLocation getStoreLocation(BlobType blobType, String extension);
    List<String> deleteAllByKey(List<String> keysList);
}
