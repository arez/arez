package com.example.deprecated;

import arez.annotations.Memoize;
import java.util.Date;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DeprecatedInterface
  extends DeprecatedBaseInterface
{
  @Memoize( name = "_isValid" )
  @Override
  @Deprecated
  default boolean isValid( @NonNull final Date at )
  {
    return false;
  }
}
