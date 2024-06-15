package io.oigres.ecomm.service.users.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * A slice of data that indicates whether there's a next or previous slice available. Allows to obtain a
 * {@link PageableRequest} to request a previous or next {@link SliceResponse}.
 *
 * @author Sergio Exposito
 */
public interface SliceResponse<T> extends Serializable {
    
	/**
	 * Returns the number of the current {@link SliceResponse}. Is always non-negative.
	 *
	 * @return the number of the current {@link SliceResponse}.
	 */
	int getNumber();

	/**
	 * Returns the size of the {@link SliceResponse}.
	 *
	 * @return the size of the {@link SliceResponse}.
	 */
	int getSize();

	/**
	 * Returns the number of elements currently on this {@link SliceResponse}.
	 *
	 * @return the number of elements currently on this {@link SliceResponse}.
	 */
	int getNumberOfElements();

	/**
	 * Returns the page content as {@link List}.
	 *
	 * @return
	 */
	List<T> getContent();

	/**
	 * Returns whether the {@link SliceResponse} has content at all.
	 *
	 * @return
	 */
	boolean hasContent();

	/**
	 * Returns the sorting parameters for the {@link SliceResponse}.
	 *
	 * @return
	 */
	SortRequest getSort();

	/**
	 * Returns whether the current {@link SliceResponse} is the first one.
	 *
	 * @return
	 */
	boolean isFirst();

	/**
	 * Returns whether the current {@link SliceResponse} is the last one.
	 *
	 * @return
	 */
	boolean isLast();

	/**
	 * Returns if there is a next {@link SliceResponse}.
	 *
	 * @return if there is a next {@link SliceResponse}.
	 */
	boolean hasNext();

	/**
	 * Returns if there is a previous {@link SliceResponse}.
	 *
	 * @return if there is a previous {@link SliceResponse}.
	 */
	boolean hasPrevious();

	/**
	 * Returns the {@link PageableRequest} that's been used to request the current {@link SliceResponse}.
	 *
	 * @return
	 * @since 2.0
	 */
	default PageableRequest getPageable() {
		return PageableRequestImpl.of(getNumber(), getSize(), getSort());
	}

	/**
	 * Returns the {@link PageableRequest} to request the next {@link SliceResponse}. Can be {@link PageableRequest#unpaged()} in case the
	 * current {@link SliceResponse} is already the last one. Clients should check {@link #hasNext()} before calling this method.
	 *
	 * @return
	 * @see #nextOrLastPageable()
	 */
	PageableRequest nextPageable();

	/**
	 * Returns the {@link PageableRequest} to request the previous {@link SliceResponse}. Can be {@link PageableRequest#unpaged()} in case the
	 * current {@link SliceResponse} is already the first one. Clients should check {@link #hasPrevious()} before calling this
	 * method.
	 *
	 * @return
	 * @see #previousPageable()
	 */
	PageableRequest previousPageable();

	/**
	 * Returns the {@link PageableRequest} describing the next slice or the one describing the current slice in case it's the
	 * last one.
	 *
	 * @return
	 * @since 2.2
	 */
	default PageableRequest nextOrLastPageable() {
		return hasNext() ? nextPageable() : getPageable();
	}

	/**
	 * Returns the {@link PageableRequest} describing the previous slice or the one describing the current slice in case it's the
	 * first one.
	 *
	 * @return
	 * @since 2.2
	 */
	default PageableRequest previousOrFirstPageable() {
		return hasPrevious() ? previousPageable() : getPageable();
	}

}
