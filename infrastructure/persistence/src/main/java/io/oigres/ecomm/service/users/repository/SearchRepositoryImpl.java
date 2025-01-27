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

package io.oigres.ecomm.service.users.repository;

import jakarta.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class SearchRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
    implements SearchRepository<T, ID> {
  private final EntityManager entityManager;

  public SearchRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
    this.entityManager = entityManager;
  }

  public SearchRepositoryImpl(
      JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }

  @Override
  public List<T> searchBy(String text, int limit, String... fields) {

    SearchResult<T> result = getSearchResult(text, limit, fields);

    return result.hits();
  }

  private SearchResult<T> getSearchResult(String text, int limit, String[] fields) {
    SearchSession searchSession = Search.session(entityManager);
    SearchResult<T> result =
        searchSession
            .search(getDomainClass())
            .where(f -> f.match().fields(fields).matching(text).fuzzy(2))
            .fetch(limit);
    return result;
  }
}
