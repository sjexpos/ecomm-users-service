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

/**
 * {@link PageableRequest} implementation to represent the absence of pagination information.
 *
 * @author Sergio Exposito
 */
public enum UnpagedRequest implements PageableRequest {
  INSTANCE;

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#isPaged()
   */
  @Override
  public boolean isPaged() {
    return false;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#previousOrFirst()
   */
  @Override
  public PageableRequest previousOrFirst() {
    return this;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#next()
   */
  @Override
  public PageableRequest next() {
    return this;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#hasPrevious()
   */
  @Override
  public boolean hasPrevious() {
    return false;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#getSort()
   */
  @Override
  public SortRequest getSort() {
    return SortRequest.unsorted();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#getPageSize()
   */
  @Override
  public int getPageSize() {
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#getPageNumber()
   */
  @Override
  public int getPageNumber() {
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#getOffset()
   */
  @Override
  public long getOffset() {
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#first()
   */
  @Override
  public PageableRequest first() {
    return this;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#withPage(int)
   */
  @Override
  public PageableRequest withPage(int pageNumber) {

    if (pageNumber == 0) {
      return this;
    }

    throw new UnsupportedOperationException();
  }
}
