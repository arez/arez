package com.example.memoize_context_parameter.intf.subpkg;

import arez.annotations.Memoize;

public interface MyInterfaceModelBase
{
  @Memoize
  default long countFromAbstract( final long time, float someOtherParameter )
  {
    return time;
  }

  default void pushMyContextVar( String var )
  {
  }

  default  void popMyContextVar( String var )
  {
  }
}
