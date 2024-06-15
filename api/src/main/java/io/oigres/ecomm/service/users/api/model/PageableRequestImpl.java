package io.oigres.ecomm.service.users.api.model;

import io.oigres.ecomm.service.users.api.model.SortRequest.Direction;

/**
 * Basic Java Bean implementation of {@link PageableRequest}.
 *
 * @author Sergio Exposito
 */
public class PageableRequestImpl extends AbstractPageableRequest {

	private static final long serialVersionUID = -4541509938956089562L;

	private final SortRequest sort;

	/**
	 * Creates a new {@link PageableRequestImpl} with sort parameters applied.
	 *
	 * @param page zero-based page index, must not be negative.
	 * @param size the size of the page to be returned, must be greater than 0.
	 * @param sort must not be {@literal null}, use {@link SortRequest#unsorted()} instead.
	 */
	protected PageableRequestImpl(int page, int size, SortRequest sort) {
		super(page, size);
		this.sort = sort;
	}

	/**
	 * Creates a new unsorted {@link PageableRequestImpl}.
	 *
	 * @param page zero-based page index, must not be negative.
	 * @param size the size of the page to be returned, must be greater than 0.
	 * @since 2.0
	 */
	public static PageableRequestImpl of(int page, int size) {
		return of(page, size, SortRequest.unsorted());
	}

	/**
	 * Creates a new {@link PageableRequestImpl} with sort parameters applied.
	 *
	 * @param page zero-based page index.
	 * @param size the size of the page to be returned.
	 * @param sort must not be {@literal null}, use {@link SortRequest#unsorted()} instead.
	 * @since 2.0
	 */
	public static PageableRequestImpl of(int page, int size, SortRequest sort) {
		return new PageableRequestImpl(page, size, sort);
	}

	/**
	 * Creates a new {@link PageableRequestImpl} with sort direction and properties applied.
	 *
	 * @param page zero-based page index, must not be negative.
	 * @param size the size of the page to be returned, must be greater than 0.
	 * @param direction must not be {@literal null}.
	 * @param properties must not be {@literal null}.
	 * @since 2.0
	 */
	public static PageableRequestImpl of(int page, int size, Direction direction, String... properties) {
		return of(page, size, SortRequest.by(direction, properties));
	}

	/**
	 * Creates a new {@link PageableRequestImpl} for the first page (page number {@code 0}) given {@code pageSize} .
	 *
	 * @param pageSize the size of the page to be returned, must be greater than 0.
	 * @return a new {@link PageableRequestImpl}.
	 * @since 2.5
	 */
	public static PageableRequestImpl ofSize(int pageSize) {
		return PageableRequestImpl.of(0, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Pageable#getSort()
	 */
	public SortRequest getSort() {
		return sort;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Pageable#next()
	 */
	@Override
	public PageableRequestImpl next() {
		return new PageableRequestImpl(getPageNumber() + 1, getPageSize(), getSort());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.AbstractPageRequest#previous()
	 */
	@Override
	public PageableRequestImpl previous() {
		return getPageNumber() == 0 ? this : new PageableRequestImpl(getPageNumber() - 1, getPageSize(), getSort());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Pageable#first()
	 */
	@Override
	public PageableRequestImpl first() {
		return new PageableRequestImpl(0, getPageSize(), getSort());
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

		if (!(obj instanceof PageableRequestImpl)) {
			return false;
		}

		PageableRequestImpl that = (PageableRequestImpl) obj;

		return super.equals(that) && this.sort.equals(that.sort);
	}

	/**
	 * Creates a new {@link PageableRequestImpl} with {@code pageNumber} applied.
	 *
	 * @param pageNumber
	 * @return a new {@link PageableRequestImpl}.
	 * @since 2.5
	 */
	@Override
	public PageableRequestImpl withPage(int pageNumber) {
		return new PageableRequestImpl(pageNumber, getPageSize(), getSort());
	}

	/**
	 * Creates a new {@link PageableRequestImpl} with {@link Direction} and {@code properties} applied.
	 *
	 * @param direction must not be {@literal null}.
	 * @param properties must not be {@literal null}.
	 * @return a new {@link PageableRequestImpl}.
	 * @since 2.5
	 */
	public PageableRequestImpl withSort(Direction direction, String... properties) {
		return new PageableRequestImpl(getPageNumber(), getPageSize(), SortRequest.by(direction, properties));
	}

	/**
	 * Creates a new {@link PageableRequestImpl} with {@link SortRequest} applied.
	 *
	 * @param sort must not be {@literal null}.
	 * @return a new {@link PageableRequestImpl}.
	 * @since 2.5
	 */
	public PageableRequestImpl withSort(SortRequest sort) {
		return new PageableRequestImpl(getPageNumber(), getPageSize(), sort);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 31 * super.hashCode() + sort.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Page request [number: %d, size %d, sort: %s]", getPageNumber(), getPageSize(), sort);
	}

}
