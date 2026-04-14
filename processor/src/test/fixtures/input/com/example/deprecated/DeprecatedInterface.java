package com.example.deprecated;

import arez.annotations.Memoize;
import java.util.Date;
import javax.annotation.Nonnull;

public interface DeprecatedInterface
  extends DeprecatedBaseInterface
{
  @Memoize( name = "_isValid" )
  @Override
  @Deprecated
  default boolean isValid( @Nonnull final Date at )
  {
    return false;
  }
}
