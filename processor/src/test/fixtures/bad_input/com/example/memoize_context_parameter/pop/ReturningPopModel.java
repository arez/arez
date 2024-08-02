package com.example.memoize_context_parameter.pop;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;

@ArezComponent
abstract class ReturningPopModel
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

  void pushMyContextVar( String var )
  {

  }

  @MemoizeContextParameter
  String popMyContextVar( String var )
  {
    return "";
  }
}
