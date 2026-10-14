package com.example.memoize_context_parameter.pop;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class StaticPopModel
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

  @Nullable String captureMyContextVar()
  {
    return "";
  }

  void pushMyContextVar( @Nullable String var )
  {
  }

  @MemoizeContextParameter
  static void popMyContextVar( @Nullable String var )
  {
  }
}
