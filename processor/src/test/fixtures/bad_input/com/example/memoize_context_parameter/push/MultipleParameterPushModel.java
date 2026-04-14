package com.example.memoize_context_parameter.push;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;

@ArezComponent
abstract class MultipleParameterPushModel
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
  void pushMyContextVar( String var, int i )
  {
  }

  void popMyContextVar( String var )
  {
  }
}
