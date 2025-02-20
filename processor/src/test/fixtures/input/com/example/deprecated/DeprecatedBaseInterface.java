package com.example.deprecated;

import arez.annotations.Memoize;
import java.util.Date;
import javax.annotation.Nonnull;

public interface DeprecatedBaseInterface
{
  @SuppressWarnings( "DeprecatedIsStillUsed" )
  @Deprecated
  boolean isValid( @Nonnull final Date at );

  @Memoize
  default boolean isValid()
  {
    return isValid( new Date() );
  }
}
