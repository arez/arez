package com.example.deprecated;

import arez.annotations.Memoize;
import java.util.Date;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DeprecatedBaseInterface
{
  @SuppressWarnings( "DeprecatedIsStillUsed" )
  @Deprecated
  boolean isValid( @NonNull final Date at );

  @Memoize
  default boolean isValid()
  {
    return isValid( new Date() );
  }
}
