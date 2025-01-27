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

import io.oigres.ecomm.service.users.api.model.PageResponse;
import io.oigres.ecomm.service.users.api.model.PageableRequestImpl;
import io.oigres.ecomm.service.users.api.model.exception.NotFoundException;
import io.oigres.ecomm.service.users.api.model.exception.StateNotFoundException;
import io.oigres.ecomm.service.users.api.model.exception.ZipcodeNotFoundException;
import io.oigres.ecomm.service.users.api.model.region.StateResponse;
import io.oigres.ecomm.service.users.api.model.region.ZipCodeResponse;
import io.swagger.v3.oas.annotations.Parameter;

public interface IRegionService {

  PageResponse<StateResponse> getStates(
      @Parameter(hidden = true, required = true) PageableRequestImpl pageable);

  PageResponse<ZipCodeResponse> getZipCodesByState(
      @Parameter(
              name = "stateId",
              required = true,
              description = "identifier associated with the state")
          Long stateId,
      @Parameter(hidden = true, required = true) PageableRequestImpl pageable)
      throws NotFoundException;

  StateResponse getStateById(long stateId) throws StateNotFoundException;

  ZipCodeResponse getZipCodeById(long zipCodeId) throws ZipcodeNotFoundException;
}
