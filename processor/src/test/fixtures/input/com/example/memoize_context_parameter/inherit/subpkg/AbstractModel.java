package com.example.memoize_context_parameter.inherit.subpkg;

import arez.annotations.Memoize;

public abstract class AbstractModel
{
  @Memoize
  public long countFromAbstract( final long time, float someOtherParameter )
  {
    return time;
  }

  protected void pushMyContextVar( String var )
  {
  }

  protected void popMyContextVar( String var )
  {
  }
}
