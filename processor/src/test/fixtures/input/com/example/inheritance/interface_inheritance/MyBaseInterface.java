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

  //String getValueA();
  //
  //String getValueC();
}
