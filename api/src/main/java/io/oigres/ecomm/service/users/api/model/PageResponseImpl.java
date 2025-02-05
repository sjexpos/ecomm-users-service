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

import java.util.List;
import lombok.Getter;

/**
 * Basic {@code Page} implementation.
 *
 * @param <T> the type of which the page consists.
 * @author Sergio Exposito
 */
public class PageResponseImpl<T> extends ChunkResponse<T> implements PageResponse<T> {

  private static final long serialVersionUID = 867755909294344406L;
  @Getter private final long total;

  /**
   * Constructor of {@code PageImpl}.
   *
   * @param content the content of this page, must not be {@literal null}.
   * @param pageable the paging information, must not be {@literal null}.
   * @param total the total amount of items available. The total might be adapted considering the length of the content
   *          given, if it is going to be the content of the last page. This is in place to mitigate inconsistencies.
   */
  public PageResponseImpl(List<T> content, PageableRequest pageable, long total) {

    super(content, pageable);

    this.total =
        pageable
            .toOptional()
            .filter(it -> !content.isEmpty()) //
            .filter(it -> it.getOffset() + it.getPageSize() > total) //
            .map(it -> it.getOffset() + content.size()) //
            .orElse(total);
  }

  /**
   * Creates a new {@link PageResponseImpl} with the given content. This will result in the created {@link PageResponse} being identical
   * to the entire {@link List}.
   *
   * @param content must not be {@literal null}.
   */
  public PageResponseImpl(List<T> content) {
    this(content, PageableRequest.unpaged(), null == content ? 0 : content.size());
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Page#getTotalPages()
   */
  @Override
  public int getTotalPages() {
    return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Page#getTotalElements()
   */
  @Override
  public long getTotalElements() {
    return total;
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Slice#hasNext()
   */
  @Override
  public boolean hasNext() {
    return getNumber() + 1 < getTotalPages();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.domain.Slice#isLast()
   */
  @Override
  public boolean isLast() {
    return !hasNext();
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {

    String contentType = "UNKNOWN";
    List<T> content = getContent();

    if (!content.isEmpty() && content.get(0) != null) {
      contentType = content.get(0).getClass().getName();
    }

    return String.format(
        "Page %s of %d containing %s instances", getNumber() + 1, getTotalPages(), contentType);
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

    if (!(obj instanceof PageResponseImpl<?>)) {
      return false;
    }

    PageResponseImpl<?> that = (PageResponseImpl<?>) obj;

    return this.total == that.total && super.equals(obj);
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {

    int result = 17;

    result += 31 * (int) (total ^ total >>> 32);
    result += 31 * super.hashCode();

    return result;
  }
}
