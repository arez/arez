package com.example.memoize_context_parameter;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;

@ArezComponent
abstract class AllowEmptyModel
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

  @MemoizeContextParameter( pattern = "NONE", allowEmpty = true )
  String captureMyContextVar()
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
