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

import java.io.Serializable;
import lombok.Getter;

/**
 * Abstract Java Bean implementation of {@code Pageable}.
 *
 * @author Sergio Exposito
 */
public abstract class AbstractPageableRequest implements PageableRequest, Serializable {

  private static final long serialVersionUID = 1232825578694716871L;

  @Getter private final int page;
  @Getter private final int size;

  /**
   * Creates a new {@link AbstractPageableRequest}. Pages are zero indexed, thus providing 0 for {@code page} will return
   * the first page.
   *
   * @param page must not be less than zero.
   * @param size must not be less than one.
   */
  public AbstractPageableRequest(int page, int size) {

    if (page < 0) {
      throw new IllegalArgumentException("Page index must not be less than zero");
    }

    if (size < 1) {
      throw new IllegalArgumentException("Page size must not be less than one");
    }

    this.page = page;
    this.size = size;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#getPageSize()
   */
  public int getPageSize() {
    return size;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#getPageNumber()
   */
  public int getPageNumber() {
    return page;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#getOffset()
   */
  public long getOffset() {
    return (long) page * (long) size;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#hasPrevious()
   */
  public boolean hasPrevious() {
    return page > 0;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#previousOrFirst()
   */
  public PageableRequest previousOrFirst() {
    return hasPrevious() ? previous() : first();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#next()
   */
  public abstract PageableRequest next();

  /**
   * Returns the {@link PageableRequest} requesting the previous {@link PageResponse}.
   *
   * @return
   */
  public abstract PageableRequest previous();

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Pageable#first()
   */
  public abstract PageableRequest first();

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;

    result = prime * result + page;
    result = prime * result + size;

    return result;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    AbstractPageableRequest other = (AbstractPageableRequest) obj;
    return this.page == other.page && this.size == other.size;
  }
}
