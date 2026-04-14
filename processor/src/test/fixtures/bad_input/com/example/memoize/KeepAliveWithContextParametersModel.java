package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;

@ArezComponent
public abstract class KeepAliveWithContextParametersModel
{
  @Memoize( keepAlive = true )
  public long getField()
  {
    return 0;
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
  {
  }
}
