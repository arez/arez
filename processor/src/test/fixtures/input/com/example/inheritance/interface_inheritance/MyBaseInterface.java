package com.example.inheritance.interface_inheritance;

import arez.annotations.Memoize;
import javax.annotation.Nonnull;

public interface MyBaseInterface
{
  @Memoize
  default boolean isValid( @Nonnull final String param )
  {
    return true;
  }

  // Adding this here will cause the potential property to be defined earlier ... but this should not re-order
  // the way the set of observables as they appear in the entity
  String getValueC();
}
