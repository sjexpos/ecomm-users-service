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

package io.oigres.ecomm.service.users.api.model;

import java.util.Collections;

/**
 * A page is a sublist of a list of objects. It allows gain information about the position of it in the containing
 * entire list.
 *
 * @param <T>
 * @author Sergio Exposito
 */
public interface PageResponse<T> extends SliceResponse<T> {

  /**
   * Creates a new empty {@link PageResponse}.
   *
   * @return
   * @since 2.0
   */
  static <T> PageResponse<T> empty() {
    return empty(PageableRequest.unpaged());
  }

  /**
   * Creates a new empty {@link PageResponse} for the given {@link PageableRequest}.
   *
   * @param pageable must not be {@literal null}.
   * @return
   * @since 2.0
   */
  static <T> PageResponse<T> empty(PageableRequest pageable) {
    return new PageResponseImpl<>(Collections.emptyList(), pageable, 0);
  }

  /**
   * Returns the number of total pages.
   *
   * @return the number of total pages
   */
  int getTotalPages();

  /**
   * Returns the total amount of elements.
   *
   * @return the total amount of elements
   */
  long getTotalElements();
}
