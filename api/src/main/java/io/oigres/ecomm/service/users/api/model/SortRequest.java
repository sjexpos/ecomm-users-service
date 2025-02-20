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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Sort option for queries. You have to provide at least a list of properties to sort for that must not include
 * {@literal null} or empty strings. The direction defaults to {@link SortRequest#DEFAULT_DIRECTION}.
 *
 * @author Sergio Exposito
 */
public class SortRequest implements Serializable {

  private static final long serialVersionUID = 5737186511678863905L;

  private static final SortRequest UNSORTED = SortRequest.by(new Order[0]);

  public static final Direction DEFAULT_DIRECTION = Direction.ASC;

  private final List<Order> orders;

  protected SortRequest(List<Order> orders) {
    this.orders = orders;
  }

  /**
   * Creates a new {@link SortRequest} instance.
   *
   * @param direction defaults to {@link SortRequest#DEFAULT_DIRECTION} (for {@literal null} cases, too)
   * @param properties must not be {@literal null} or contain {@literal null} or empty strings.
   */
  private SortRequest(Direction direction, List<String> properties) {

    if (properties == null || properties.isEmpty()) {
      throw new IllegalArgumentException("You have to provide at least one property to sort by");
    }

    this.orders =
        properties.stream() //
            .map(it -> new Order(direction, it)) //
            .collect(Collectors.toList());
  }

  /**
   * Creates a new {@link SortRequest} for the given properties.
   *
   * @param properties must not be {@literal null}.
   * @return
   */
  public static SortRequest by(String... properties) {
    return properties.length == 0 //
        ? SortRequest.unsorted() //
        : new SortRequest(DEFAULT_DIRECTION, Arrays.asList(properties));
  }

  /**
   * Creates a new {@link SortRequest} for the given {@link Order}s.
   *
   * @param orders must not be {@literal null}.
   * @return
   */
  public static SortRequest by(List<Order> orders) {
    return orders.isEmpty() ? SortRequest.unsorted() : new SortRequest(orders);
  }

  /**
   * Creates a new {@link SortRequest} for the given {@link Order}s.
   *
   * @param orders must not be {@literal null}.
   * @return
   */
  public static SortRequest by(Order... orders) {
    return new SortRequest(Arrays.asList(orders));
  }

  /**
   * Creates a new {@link SortRequest} for the given {@link Order}s.
   *
   * @param direction must not be {@literal null}.
   * @param properties must not be {@literal null}.
   * @return
   */
  public static SortRequest by(Direction direction, String... properties) {
    return SortRequest.by(
        Arrays.stream(properties) //
            .map(it -> new Order(direction, it)) //
            .collect(Collectors.toList()));
  }

  /**
   * Returns a {@link SortRequest} instances representing no sorting setup at all.
   *
   * @return
   */
  public static SortRequest unsorted() {
    return UNSORTED;
  }

  /**
   * Returns a new {@link SortRequest} with the current setup but descending order direction.
   *
   * @return
   */
  public SortRequest descending() {
    return withDirection(Direction.DESC);
  }

  /**
   * Returns a new {@link SortRequest} with the current setup but ascending order direction.
   *
   * @return
   */
  public SortRequest ascending() {
    return withDirection(Direction.ASC);
  }

  public boolean isSorted() {
    return !isEmpty();
  }

  public boolean isEmpty() {
    return orders.isEmpty();
  }

  public boolean isUnsorted() {
    return !isSorted();
  }

  public List<Order> getOrders() {
    return List.copyOf(this.orders);
  }

  public SortRequest and(SortRequest sort) {
    List<Order> these = new ArrayList<Order>(this.orders);

    for (Order order : sort.orders) {
      these.add(order);
    }

    return SortRequest.by(these);
  }

  /**
   * Creates a new {@link SortRequest} with the current setup but the given order direction.
   *
   * @param direction
   * @return
   */
  private SortRequest withDirection(Direction direction) {

    return SortRequest.by(
        this.orders.stream().map(it -> it.with(direction)).collect(Collectors.toList()));
  }

  /**
   * Enumeration for sort directions.
   *
   * @author Oliver Gierke
   */
  public enum Direction {
    ASC,
    DESC;

    /**
     * Returns whether the direction is ascending.
     *
     * @return
     * @since 1.13
     */
    public boolean isAscending() {
      return this.equals(ASC);
    }

    /**
     * Returns whether the direction is descending.
     *
     * @return
     * @since 1.13
     */
    public boolean isDescending() {
      return this.equals(DESC);
    }

    /**
     * Returns the {@link Direction} enum for the given {@link String} value.
     *
     * @param value
     * @throws IllegalArgumentException in case the given value cannot be parsed into an enum value.
     * @return
     */
    public static Direction fromString(String value) {

      try {
        return Direction.valueOf(value.toUpperCase(Locale.US));
      } catch (Exception e) {
        throw new IllegalArgumentException(
            String.format(
                "Invalid value '%s' for orders given; Has to be either 'desc' or 'asc' (case"
                    + " insensitive)",
                value),
            e);
      }
    }

    /**
     * Returns the {@link Direction} enum for the given {@link String} or null if it cannot be parsed into an enum
     * value.
     *
     * @param value
     * @return
     */
    public static Optional<Direction> fromOptionalString(String value) {

      try {
        return Optional.of(fromString(value));
      } catch (IllegalArgumentException e) {
        return Optional.empty();
      }
    }
  }

  /**
   * Enumeration for null handling hints that can be used in {@link Order} expressions.
   *
   * @author Thomas Darimont
   * @since 1.8
   */
  public static enum NullHandling {

    /**
     * Lets the data store decide what to do with nulls.
     */
    NATIVE,

    /**
     * A hint to the used data store to order entries with null values before non null entries.
     */
    NULLS_FIRST,

    /**
     * A hint to the used data store to order entries with null values after non null entries.
     */
    NULLS_LAST;
  }

  /**
   * PropertyPath implements the pairing of an {@link Direction} and a property. It is used to provide input for
   * {@link SortRequest}
   *
   * @author Oliver Gierke
   * @author Kevin Raymond
   */
  public static class Order implements Serializable {

    private static final long serialVersionUID = 1522511010900108987L;
    private static final boolean DEFAULT_IGNORE_CASE = false;
    private static final NullHandling DEFAULT_NULL_HANDLING = NullHandling.NATIVE;

    private final Direction direction;
    private final String property;
    private final boolean ignoreCase;
    private final NullHandling nullHandling;

    /**
     * Creates a new {@link Order} instance. if order is {@literal null} then order defaults to
     * {@link SortRequest#DEFAULT_DIRECTION}
     *
     * @param direction can be {@literal null}, will default to {@link SortRequest#DEFAULT_DIRECTION}
     * @param property must not be {@literal null} or empty.
     */
    public Order(Direction direction, String property) {
      this(direction, property, DEFAULT_IGNORE_CASE, DEFAULT_NULL_HANDLING);
    }

    /**
     * Creates a new {@link Order} instance. if order is {@literal null} then order defaults to
     * {@link SortRequest#DEFAULT_DIRECTION}
     *
     * @param direction can be {@literal null}, will default to {@link SortRequest#DEFAULT_DIRECTION}
     * @param property must not be {@literal null} or empty.
     * @param nullHandlingHint must not be {@literal null}.
     */
    public Order(Direction direction, String property, NullHandling nullHandlingHint) {
      this(direction, property, DEFAULT_IGNORE_CASE, nullHandlingHint);
    }

    /**
     * Creates a new {@link Order} instance. Takes a single property. Direction defaults to
     * {@link SortRequest#DEFAULT_DIRECTION}.
     *
     * @param property must not be {@literal null} or empty.
     * @since 2.0
     */
    public static Order by(String property) {
      return new Order(DEFAULT_DIRECTION, property);
    }

    /**
     * Creates a new {@link Order} instance. Takes a single property. Direction is {@link Direction#ASC} and
     * NullHandling {@link NullHandling#NATIVE}.
     *
     * @param property must not be {@literal null} or empty.
     * @since 2.0
     */
    public static Order asc(String property) {
      return new Order(Direction.ASC, property, DEFAULT_NULL_HANDLING);
    }

    /**
     * Creates a new {@link Order} instance. Takes a single property. Direction is {@link Direction#DESC} and
     * NullHandling {@link NullHandling#NATIVE}.
     *
     * @param property must not be {@literal null} or empty.
     * @since 2.0
     */
    public static Order desc(String property) {
      return new Order(Direction.DESC, property, DEFAULT_NULL_HANDLING);
    }

    /**
     * Creates a new {@link Order} instance. if order is {@literal null} then order defaults to
     * {@link SortRequest#DEFAULT_DIRECTION}
     *
     * @param direction can be {@literal null}, will default to {@link SortRequest#DEFAULT_DIRECTION}
     * @param property must not be {@literal null} or empty.
     * @param ignoreCase true if sorting should be case-insensitive. false if sorting should be case-sensitive.
     * @param nullHandling must not be {@literal null}.
     * @since 1.7
     */
    private Order(
        Direction direction, String property, boolean ignoreCase, NullHandling nullHandling) {

      if (property == null || property.trim().isEmpty()) {
        throw new IllegalArgumentException("Property must not be null or empty");
      }

      this.direction = direction == null ? DEFAULT_DIRECTION : direction;
      this.property = property;
      this.ignoreCase = ignoreCase;
      this.nullHandling = nullHandling;
    }

    /**
     * Returns the order the property shall be sorted for.
     *
     * @return
     */
    public Direction getDirection() {
      return direction;
    }

    /**
     * Returns the property to order for.
     *
     * @return
     */
    public String getProperty() {
      return property;
    }

    /**
     * Returns whether sorting for this property shall be ascending.
     *
     * @return
     */
    public boolean isAscending() {
      return this.direction.isAscending();
    }

    /**
     * Returns whether sorting for this property shall be descending.
     *
     * @return
     * @since 1.13
     */
    public boolean isDescending() {
      return this.direction.isDescending();
    }

    /**
     * Returns whether the sort will be case-sensitive or case-insensitive.
     *
     * @return
     */
    public boolean isIgnoreCase() {
      return ignoreCase;
    }

    /**
     * Returns a new {@link Order} with the given {@link Direction}.
     *
     * @param direction
     * @return
     */
    public Order with(Direction direction) {
      return new Order(direction, this.property, this.ignoreCase, this.nullHandling);
    }

    /**
     * Returns a new {@link Order}
     *
     * @param property must not be {@literal null} or empty.
     * @return
     * @since 1.13
     */
    public Order withProperty(String property) {
      return new Order(this.direction, property, this.ignoreCase, this.nullHandling);
    }

    /**
     * Returns a new {@link SortRequest} instance for the given properties.
     *
     * @param properties
     * @return
     */
    public SortRequest withProperties(String... properties) {
      return SortRequest.by(this.direction, properties);
    }

    /**
     * Returns a new {@link Order} with case insensitive sorting enabled.
     *
     * @return
     */
    public Order ignoreCase() {
      return new Order(direction, property, true, nullHandling);
    }

    /**
     * Returns a {@link Order} with the given {@link NullHandling}.
     *
     * @param nullHandling can be {@literal null}.
     * @return
     * @since 1.8
     */
    public Order with(NullHandling nullHandling) {
      return new Order(direction, this.property, ignoreCase, nullHandling);
    }

    /**
     * Returns a {@link Order} with {@link NullHandling#NULLS_FIRST} as null handling hint.
     *
     * @return
     * @since 1.8
     */
    public Order nullsFirst() {
      return with(NullHandling.NULLS_FIRST);
    }

    /**
     * Returns a {@link Order} with {@link NullHandling#NULLS_LAST} as null handling hint.
     *
     * @return
     * @since 1.7
     */
    public Order nullsLast() {
      return with(NullHandling.NULLS_LAST);
    }

    /**
     * Returns a {@link Order} with {@link NullHandling#NATIVE} as null handling hint.
     *
     * @return
     * @since 1.7
     */
    public Order nullsNative() {
      return with(NullHandling.NATIVE);
    }

    /**
     * Returns the used {@link NullHandling} hint, which can but may not be respected by the used datastore.
     *
     * @return
     * @since 1.7
     */
    public NullHandling getNullHandling() {
      return nullHandling;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

      int result = 17;

      result = 31 * result + direction.hashCode();
      result = 31 * result + property.hashCode();
      result = 31 * result + (ignoreCase ? 1 : 0);
      result = 31 * result + nullHandling.hashCode();

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

      if (!(obj instanceof Order)) {
        return false;
      }

      Order that = (Order) obj;

      return this.direction.equals(that.direction)
          && this.property.equals(that.property)
          && this.ignoreCase == that.ignoreCase
          && this.nullHandling.equals(that.nullHandling);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

      String result = String.format("%s: %s", property, direction);

      if (!NullHandling.NATIVE.equals(nullHandling)) {
        result += ", " + nullHandling;
      }

      if (ignoreCase) {
        result += ", ignoring case";
      }

      return result;
    }
  }
}
