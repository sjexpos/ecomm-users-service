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
import io.oigres.ecomm.service.users.api.model.PageableRequestImpl;
import io.oigres.ecomm.service.users.api.model.exception.NotFoundException;
import io.oigres.ecomm.service.users.api.model.exception.StateNotFoundException;
import io.oigres.ecomm.service.users.api.model.exception.ZipcodeNotFoundException;
import io.oigres.ecomm.service.users.api.model.region.StateResponse;
import io.oigres.ecomm.service.users.api.model.region.ZipCodeResponse;
import java.net.URI;
import java.util.concurrent.Future;
import java.util.function.Function;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

public class RegionServiceProxy extends MiddlewareProxy
    implements IRegionService, AsyncRegionService {

  public RegionServiceProxy(WebClient webClient) {
    super(webClient);
  }

  // --------------------------------- getStates --------------------------------- //

  @Override
  public PageResponse<StateResponse> getStates(PageableRequestImpl pageable) {
    return getPage(
        uriBuilder ->
            uriBuilder
                .path(Routes.REGION_PATH.concat(Routes.GET_STATES))
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .build(pageable),
        StateResponse.class);
  }

  // --------------------------------- getZipCodesByState --------------------------------- //

  @Override
  public PageResponse<ZipCodeResponse> getZipCodesByState(
      Long stateId, PageableRequestImpl pageable) throws NotFoundException {
    return getPage(
        uriBuilder ->
            uriBuilder
                .path(Routes.REGION_PATH.concat(Routes.ZIP_CODES))
                .queryParam("stateId", stateId)
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .build(stateId, pageable),
        ZipCodeResponse.class);
  }

  // --------------------------------- getStateById --------------------------------- //

  private Function<UriBuilder, URI> getStateById_Call(long stateId) {
    return uriBuilder ->
        uriBuilder.path(Routes.REGION_PATH.concat(Routes.GET_STATE_BY_ID)).build(stateId);
  }

  @Override
  public StateResponse getStateById(long stateId) throws StateNotFoundException {
    return get(getStateById_Call(stateId), StateResponse.class);
  }

  @Override
  public Future<StateResponse> getStateByIdAsync(long stateId) throws StateNotFoundException {
    return getAsync(getStateById_Call(stateId), StateResponse.class);
  }

  // --------------------------------- getZipCodeById --------------------------------- //

  private Function<UriBuilder, URI> getZipCodeById_Call(long zipCodeId) {
    return uriBuilder ->
        uriBuilder.path(Routes.REGION_PATH.concat(Routes.GET_ZIPCODE_BY_ID)).build(zipCodeId);
  }

  @Override
  public ZipCodeResponse getZipCodeById(long zipCodeId) throws ZipcodeNotFoundException {
    return get(getZipCodeById_Call(zipCodeId), ZipCodeResponse.class);
  }

  @Override
  public Future<ZipCodeResponse> getZipCodeByIdAsync(long zipCodeId)
      throws ZipcodeNotFoundException {
    return getAsync(getZipCodeById_Call(zipCodeId), ZipCodeResponse.class);
  }
}
