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
