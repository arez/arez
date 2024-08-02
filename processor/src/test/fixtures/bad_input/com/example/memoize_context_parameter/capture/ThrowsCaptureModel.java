package com.example.memoize_context_parameter.capture;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;

@ArezComponent
abstract class ThrowsCaptureModel
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
  String captureMyContextVar()
    throws RuntimeException
  {
    return "";
  }

  void pushMyContextVar( String var )
  {
  }

  void popMyContextVar( String var )
  {
  }
}
