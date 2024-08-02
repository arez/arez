package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import arez.annotations.OnDeactivate;

@ArezComponent
public abstract class MemoizeHasContextParametersModel
{
  @Memoize
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate
  void onMyValueDeactivate()
  {
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
