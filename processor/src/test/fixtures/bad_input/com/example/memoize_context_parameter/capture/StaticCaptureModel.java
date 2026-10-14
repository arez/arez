package com.example.memoize_context_parameter.capture;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class StaticCaptureModel
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

  @MemoizeContextParameter
  static @Nullable String captureMyContextVar()
  {
    return "";
  }

  void pushMyContextVar( @Nullable String var )
  {
  }

  void popMyContextVar( @Nullable String var )
  {
  }
}
