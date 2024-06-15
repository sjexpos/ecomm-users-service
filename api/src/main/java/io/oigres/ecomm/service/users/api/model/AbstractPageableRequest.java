package io.oigres.ecomm.service.users.api.model;

import lombok.Getter;

import java.io.Serializable;

/**
 * Abstract Java Bean implementation of {@code Pageable}.
 *
 * @author Sergio Exposito
 */
public abstract class AbstractPageableRequest implements PageableRequest, Serializable {

	private static final long serialVersionUID = 1232825578694716871L;

	@Getter
	private final int page;
	@Getter
	private final int size;

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
