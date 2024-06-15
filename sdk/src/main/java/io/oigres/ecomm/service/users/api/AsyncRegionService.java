package io.oigres.ecomm.service.users.api;

import java.util.concurrent.Future;

import io.oigres.ecomm.service.users.api.model.exception.StateNotFoundException;
import io.oigres.ecomm.service.users.api.model.exception.ZipcodeNotFoundException;
import io.oigres.ecomm.service.users.api.model.region.StateResponse;
import io.oigres.ecomm.service.users.api.model.region.ZipCodeResponse;

public interface AsyncRegionService {
    Future<StateResponse> getStateByIdAsync(long stateId) throws StateNotFoundException;

    Future<ZipCodeResponse> getZipCodeByIdAsync(long zipCodeId) throws ZipcodeNotFoundException;
}
