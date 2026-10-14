package com.example.memoize_context_parameter.push;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ReturningPushModel
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

  @MemoizeContextParameter
  @Nullable String pushMyContextVar( @Nullable String var )
  {
    return "";
  }

  void popMyContextVar( @Nullable String var )
  {
  }
}
