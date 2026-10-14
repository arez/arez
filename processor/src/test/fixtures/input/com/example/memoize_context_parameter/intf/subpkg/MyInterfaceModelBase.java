package com.example.memoize_context_parameter.intf.subpkg;

import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface MyInterfaceModelBase
{
  @Memoize
  default long countFromAbstract( final long time, float someOtherParameter )
  {
    return time;
  }

  default void pushMyContextVar( @Nullable String var )
  {
  }

  default  void popMyContextVar( @Nullable String var )
  {
  }
}
