package com.example.memoize_context_parameter.push;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;

@ArezComponent
abstract class AbstractPushModel
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

  String captureMyContextVar()
  {
    return "";
  }

  @MemoizeContextParameter
  abstract void pushMyContextVar( String var );

  void popMyContextVar( String var )
  {
  }
}
