package org.ardverk.enums;

/**
 * An interface for {@link Enum}s that provide an alternative {@link Object}
 * based value of themselves.
 */
public interface ObjectEnum<T> {

  /**
   * Returns the {@link Enum}'s alternative value.
   */
  public T convert();
}
