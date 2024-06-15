package io.oigres.ecomm.service.users.api;

import io.oigres.ecomm.service.users.api.model.PageResponse;
import io.oigres.ecomm.service.users.api.model.PageableRequestImpl;
import io.oigres.ecomm.service.users.api.model.exception.NotFoundException;
import io.oigres.ecomm.service.users.api.model.exception.StateNotFoundException;
import io.oigres.ecomm.service.users.api.model.exception.ZipcodeNotFoundException;
import io.oigres.ecomm.service.users.api.model.region.StateResponse;
import io.oigres.ecomm.service.users.api.model.region.ZipCodeResponse;
import io.swagger.v3.oas.annotations.Parameter;

public interface IRegionService {

    PageResponse<StateResponse> getStates(@Parameter(hidden = true, required = true) PageableRequestImpl pageable);
    PageResponse<ZipCodeResponse> getZipCodesByState(
            @Parameter(name = "stateId",required = true, description = "identifier associated with the state") Long stateId,
            @Parameter(hidden = true, required = true) PageableRequestImpl pageable) throws NotFoundException;
    StateResponse getStateById(long stateId) throws StateNotFoundException;
    ZipCodeResponse getZipCodeById(long zipCodeId) throws ZipcodeNotFoundException;
}
