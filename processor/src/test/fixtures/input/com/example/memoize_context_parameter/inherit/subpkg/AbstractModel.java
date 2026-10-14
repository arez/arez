package com.example.memoize_context_parameter.inherit.subpkg;

import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class AbstractModel
{
  @Memoize
  public long countFromAbstract( final long time, float someOtherParameter )
  {
    return time;
  }

  protected void pushMyContextVar( @Nullable String var )
  {
  }

  protected void popMyContextVar( @Nullable String var )
  {
  }
}
