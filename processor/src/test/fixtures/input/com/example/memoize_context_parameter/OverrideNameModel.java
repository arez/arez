package com.example.memoize_context_parameter;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class OverrideNameModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @Memoize
  public long count( final long time, float someOtherParameter )
  {
    return time;
  }

  @MemoizeContextParameter(name = "foo")
  @Nullable String captureSomeVar()
  {
    return "";
  }

  void pushFoo( @Nullable String var )
  {
  }

  void popFoo( @Nullable String var )
  {
  }
}
