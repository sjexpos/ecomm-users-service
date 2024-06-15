package io.oigres.ecomm.service.users.api.model;

import java.util.Optional;

/**
 * Abstract interface for pagination information.
 *
 * @author Sergio Exposito
 */
public interface PageableRequest {
    
	/**
	 * Returns a {@link PageableRequest} instance representing no pagination setup.
	 *
	 * @return
	 */
	static PageableRequest unpaged() {
		return UnpagedRequest.INSTANCE;
	}

	/**
	 * Creates a new {@link PageableRequest} for the first page (page number {@code 0}) given {@code pageSize} .
	 *
	 * @param pageSize the size of the page to be returned, must be greater than 0.
	 * @return a new {@link PageableRequest}.
	 * @since 2.5
	 */
	static PageableRequest ofSize(int pageSize) {
		return PageableRequestImpl.of(0, pageSize);
	}

	/**
	 * Returns whether the current {@link PageableRequest} contains pagination information.
	 *
	 * @return
	 */
	default boolean isPaged() {
		return true;
	}

	/**
	 * Returns whether the current {@link PageableRequest} does not contain pagination information.
	 *
	 * @return
	 */
	default boolean isUnpaged() {
		return !isPaged();
	}

	/**
	 * Returns the page to be returned.
	 *
	 * @return the page to be returned.
	 */
	int getPageNumber();

	/**
	 * Returns the number of items to be returned.
	 *
	 * @return the number of items of that page
	 */
	int getPageSize();

	/**
	 * Returns the offset to be taken according to the underlying page and page size.
	 *
	 * @return the offset to be taken
	 */
	long getOffset();

	/**
	 * Returns the sorting parameters.
	 *
	 * @return
	 */
	SortRequest getSort();

	/**
	 * Returns the current {@link SortRequest} or the given one if the current one is unsorted.
	 *
	 * @param sort must not be {@literal null}.
	 * @return
	 */
	default SortRequest getSortOr(SortRequest sort) {
		return getSort().isSorted() ? getSort() : sort;
	}

	/**
	 * Returns the {@link PageableRequest} requesting the next {@link PageResponse}.
	 *
	 * @return
	 */
	PageableRequest next();

	/**
	 * Returns the previous {@link PageableRequest} or the first {@link PageableRequest} if the current one already is the first one.
	 *
	 * @return
	 */
	PageableRequest previousOrFirst();

	/**
	 * Returns the {@link PageableRequest} requesting the first page.
	 *
	 * @return
	 */
	PageableRequest first();

	/**
	 * Creates a new {@link PageableRequest} with {@code pageNumber} applied.
	 *
	 * @param pageNumber
	 * @return a new {@link PageableRequestImpl}.
	 * @since 2.5
	 */
	PageableRequest withPage(int pageNumber);

	/**
	 * Returns whether there's a previous {@link PageableRequest} we can access from the current one. Will return
	 * {@literal false} in case the current {@link PageableRequest} already refers to the first page.
	 *
	 * @return
	 */
	boolean hasPrevious();

	/**
	 * Returns an {@link Optional} so that it can easily be mapped on.
	 *
	 * @return
	 */
	default Optional<PageableRequest> toOptional() {
		return isUnpaged() ? Optional.empty() : Optional.of(this);
	}

}
