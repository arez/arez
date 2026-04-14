package com.example.memoize_context_parameter.pop;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;

@ArezComponent
abstract class ThrowsPopModel
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
  {
    return "";
  }

  void pushMyContextVar( String var )
  {
  }

  void popMyContextVar( String var )
    throws RuntimeException
  {
  }
}
