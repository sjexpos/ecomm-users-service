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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A chunk of data restricted by the configured {@link PageableRequest}.
 *
 * @param <T>
 * @author Sergio Exposito
 */
public abstract class ChunkResponse<T> implements SliceResponse<T> {
  private static final long serialVersionUID = 867755909294344406L;

  private final List<T> content = new ArrayList<>();
  private final PageableRequest pageable;

  /**
   * Creates a new {@link ChunkResponse} with the given content and the given governing {@link PageableRequest}.
   *
   * @param content must not be {@literal null}.
   * @param pageable must not be {@literal null}.
   */
  public ChunkResponse(List<T> content, PageableRequest pageable) {
    this.content.addAll(content);
    this.pageable = pageable;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Slice#getNumber()
   */
  public int getNumber() {
    return pageable.isPaged() ? pageable.getPageNumber() : 0;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Slice#getSize()
   */
  public int getSize() {
    return pageable.isPaged() ? pageable.getPageSize() : content.size();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Slice#getNumberOfElements()
   */
  public int getNumberOfElements() {
    return content.size();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Slice#hasPrevious()
   */
  public boolean hasPrevious() {
    return getNumber() > 0;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Slice#isFirst()
   */
  public boolean isFirst() {
    return !hasPrevious();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Slice#isLast()
   */
  public boolean isLast() {
    return !hasNext();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Slice#nextPageable()
   */
  public PageableRequest nextPageable() {
    return hasNext() ? pageable.next() : PageableRequest.unpaged();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Slice#previousPageable()
   */
  public PageableRequest previousPageable() {
    return hasPrevious() ? pageable.previousOrFirst() : PageableRequest.unpaged();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Slice#hasContent()
   */
  public boolean hasContent() {
    return !content.isEmpty();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Slice#getContent()
   */
  public List<T> getContent() {
    return Collections.unmodifiableList(content);
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Slice#getPageable()
   */
  @Override
  public PageableRequest getPageable() {
    return pageable;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Slice#getSort()
   */
  @Override
  public SortRequest getSort() {
    return pageable.getSort();
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

    if (!(obj instanceof ChunkResponse<?>)) {
      return false;
    }

    ChunkResponse<?> that = (ChunkResponse<?>) obj;

    boolean contentEqual = this.content.equals(that.content);
    boolean pageableEqual = this.pageable.equals(that.pageable);

    return contentEqual && pageableEqual;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {

    int result = 17;

    result += 31 * pageable.hashCode();
    result += 31 * content.hashCode();

    return result;
  }
}
