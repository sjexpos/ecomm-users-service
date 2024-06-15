package io.oigres.ecomm.service.users.api;

import io.oigres.ecomm.service.users.Routes;
import io.oigres.ecomm.service.users.api.IRegionService;
import io.oigres.ecomm.service.users.api.model.PageResponse;
import io.oigres.ecomm.service.users.api.model.PageResponseImpl;
import io.oigres.ecomm.service.users.api.model.PageableRequestImpl;
import io.oigres.ecomm.service.users.api.model.exception.StateNotFoundException;
import io.oigres.ecomm.service.users.api.model.exception.ZipcodeNotFoundException;
import io.oigres.ecomm.service.users.api.model.region.StateResponse;
import io.oigres.ecomm.service.users.api.model.region.ZipCodeResponse;
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
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Routes.REGION_PATH)
@Tag(name = "Region", description = " ")
@Slf4j
public class RegionController extends AbstractController implements IRegionService {

    private final GetStatesUseCase getStatesUseCase;
    private final GetStateByIdUseCase getStateByIdUseCase;
    private final GetZipCodesByStateUseCase getZipCodesByState;
    private final GetZipCodeByIdUseCase getZipCodeByIdUseCase;

    private final ModelMapper mapper;

    public RegionController(GetStatesUseCase getStatesUseCase, GetStateByIdUseCase getStateByIdUseCase, GetZipCodesByStateUseCase getZipCodesByState, GetZipCodeByIdUseCase getZipCodeByIdUseCase,
                            ModelMapper mapper) {
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
    public PageResponse<StateResponse> getStates(@Parameter(hidden = true, required = true) PageableRequestImpl pageable) {
        log.debug("============ Get All States ============");
        Page<State> states = this.getStatesUseCase.handle(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending()));
        List<StateResponse> response = states.getContent().stream().map(state -> this.mapper.map(state, StateResponse.class)).collect(Collectors.toList());
        return new PageResponseImpl<>(response, pageable, states.getTotalElements());
    }

    @Operation(summary = "Get state by")
    @GetMapping(value = Routes.GET_STATE_BY_ID, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public StateResponse getStateById(@PathVariable("stateId") long stateId) throws StateNotFoundException {
        log.debug("============ getStateById ============");
        State state;
        try {
            state = getStateByIdUseCase.handle(stateId);
        } catch (NotFoundException e) {
            throw new StateNotFoundException();
        }
        return StateResponse.builder().id(state.getId()).shortName(state.getShortName()).name(state.getName()).build();
    }

    @Operation(summary = "Get zip codes by state")
    @PageableAsQueryParam
    @GetMapping(value = Routes.ZIP_CODES, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PageResponse<ZipCodeResponse> getZipCodesByState(@PathVariable("stateId") Long stateId,
            @Parameter(hidden = true, required = true) PageableRequestImpl pageable) {
        log.debug("======= GetZipCodes | stateId={} page={} size={} =====", stateId, pageable.getPageNumber(), pageable.getPageSize());
        Page<ZipCode> zipCodes = this.getZipCodesByState.handle(stateId, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending()));
        List<ZipCodeResponse> response = zipCodes.getContent().stream().map(zipCode -> this.mapper.map(zipCode, ZipCodeResponse.class)).collect(Collectors.toList());
        return new PageResponseImpl<>(response, pageable, zipCodes.getTotalElements());
    }

    @Operation(summary = "Get zipcode by id")
    @GetMapping(value = Routes.GET_ZIPCODE_BY_ID, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public ZipCodeResponse getZipCodeById(@PathVariable("zipCodeId") long zipCodeId) throws ZipcodeNotFoundException {
        log.debug("============ getZipCodeById ============");
        ZipCode zipCode;
        try {
            zipCode = getZipCodeByIdUseCase.handle(zipCodeId);
        } catch (ZipcodeNotFoundDomainException ex) {
            throw new ZipcodeNotFoundException(ex.getMessage());
        }
        return ZipCodeResponse.builder().id(zipCode.getId()).code(zipCode.getCode()).city(zipCode.getCity())
                .state(StateResponse.builder().id(zipCode.getState().getId()).shortName(zipCode.getState().getShortName()).name(zipCode.getState().getName()).build()).tax(zipCode.getTax()).build();
    }
}
