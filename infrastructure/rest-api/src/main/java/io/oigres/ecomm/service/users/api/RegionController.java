/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package io.oigres.ecomm.service.users.api;

import io.oigres.ecomm.service.users.Routes;
import io.oigres.ecomm.service.users.api.model.PageResponse;
import io.oigres.ecomm.service.users.api.model.PageResponseImpl;
import io.oigres.ecomm.service.users.api.model.PageableRequestImpl;
import io.oigres.ecomm.service.users.api.model.exception.StateNotFoundException;
import io.oigres.ecomm.service.users.api.model.exception.ZipcodeNotFoundException;
import io.oigres.ecomm.service.users.api.model.region.StateResponse;
import io.oigres.ecomm.service.users.api.model.region.ZipCodeResponse;
import io.oigres.ecomm.service.users.config.mapper.ResponsesMapper;
import io.oigres.ecomm.service.users.domain.State;
import io.oigres.ecomm.service.users.domain.ZipCode;
import io.oigres.ecomm.service.users.exception.NotFoundException;
import io.oigres.ecomm.service.users.exception.ZipcodeNotFoundDomainException;
import io.oigres.ecomm.service.users.usecases.region.search.GetStateByIdUseCase;
import io.oigres.ecomm.service.users.usecases.region.search.GetStatesUseCase;
import io.oigres.ecomm.service.users.usecases.region.search.GetZipCodeByIdUseCase;
import io.oigres.ecomm.service.users.usecases.region.search.GetZipCodesByStateUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Routes.REGION_PATH)
@Tag(name = "Region", description = " ")
@Slf4j
public class RegionController extends AbstractController implements IRegionService {

  private final GetStatesUseCase getStatesUseCase;
  private final GetStateByIdUseCase getStateByIdUseCase;
  private final GetZipCodesByStateUseCase getZipCodesByState;
  private final GetZipCodeByIdUseCase getZipCodeByIdUseCase;
  private final ResponsesMapper mapper;

  public RegionController(
      GetStatesUseCase getStatesUseCase,
      GetStateByIdUseCase getStateByIdUseCase,
      GetZipCodesByStateUseCase getZipCodesByState,
      GetZipCodeByIdUseCase getZipCodeByIdUseCase,
      ResponsesMapper mapper) {
    this.getStatesUseCase = getStatesUseCase;
    this.getStateByIdUseCase = getStateByIdUseCase;
    this.getZipCodesByState = getZipCodesByState;
    this.getZipCodeByIdUseCase = getZipCodeByIdUseCase;
    this.mapper = mapper;
  }

  @Operation(summary = "Get all States")
  @PageableAsQueryParam
  @GetMapping(value = Routes.GET_STATES, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Override
  public PageResponse<StateResponse> getStates(
      @Parameter(hidden = true, required = true) PageableRequestImpl pageable) {
    log.debug("============ Get All States ============");
    Page<State> states =
        this.getStatesUseCase.handle(
            PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending()));
    List<StateResponse> response = this.mapper.toStateResponse(states.getContent());
    return new PageResponseImpl<>(response, pageable, states.getTotalElements());
  }

  @Operation(summary = "Get state by")
  @GetMapping(value = Routes.GET_STATE_BY_ID, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Override
  public StateResponse getStateById(@PathVariable("stateId") long stateId)
      throws StateNotFoundException {
    log.debug("============ getStateById ============");
    State state;
    try {
      state = getStateByIdUseCase.handle(stateId);
    } catch (NotFoundException e) {
      throw new StateNotFoundException();
    }
    return StateResponse.builder()
        .id(state.getId())
        .shortName(state.getShortName())
        .name(state.getName())
        .build();
  }

  @Operation(summary = "Get zip codes by state")
  @PageableAsQueryParam
  @GetMapping(value = Routes.ZIP_CODES, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Override
  public PageResponse<ZipCodeResponse> getZipCodesByState(
      @PathVariable("stateId") Long stateId,
      @Parameter(hidden = true, required = true) PageableRequestImpl pageable) {
    log.debug(
        "======= GetZipCodes | stateId={} page={} size={} =====",
        stateId,
        pageable.getPageNumber(),
        pageable.getPageSize());
    Page<ZipCode> zipCodes =
        this.getZipCodesByState.handle(
            stateId,
            PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending()));
    List<ZipCodeResponse> response = this.mapper.toZipCodeResponse(zipCodes.getContent());
    return new PageResponseImpl<>(response, pageable, zipCodes.getTotalElements());
  }

  @Operation(summary = "Get zipcode by id")
  @GetMapping(value = Routes.GET_ZIPCODE_BY_ID, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Override
  public ZipCodeResponse getZipCodeById(@PathVariable("zipCodeId") long zipCodeId)
      throws ZipcodeNotFoundException {
    log.debug("============ getZipCodeById ============");
    ZipCode zipCode;
    try {
      zipCode = getZipCodeByIdUseCase.handle(zipCodeId);
    } catch (ZipcodeNotFoundDomainException ex) {
      throw new ZipcodeNotFoundException(ex.getMessage());
    }
    return ZipCodeResponse.builder()
        .id(zipCode.getId())
        .code(zipCode.getCode())
        .city(zipCode.getCity())
        .state(
            StateResponse.builder()
                .id(zipCode.getState().getId())
                .shortName(zipCode.getState().getShortName())
                .name(zipCode.getState().getName())
                .build())
        .tax(zipCode.getTax())
        .build();
  }
}
