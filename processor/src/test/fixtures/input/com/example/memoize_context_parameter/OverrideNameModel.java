package com.example.memoize_context_parameter;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;

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
  String captureSomeVar()
  {
    return "";
  }

  void pushFoo( String var )
  {
  }

  void popFoo( String var )
  {
  }
}
