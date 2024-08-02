package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import arez.annotations.OnActivate;

@ArezComponent
public abstract class MemoizeHasContextParametersModel
{
  @Memoize
  public int getMyValue()
  {
    return 0;
  }

  @OnActivate
  final void onMyValueActivate()
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
