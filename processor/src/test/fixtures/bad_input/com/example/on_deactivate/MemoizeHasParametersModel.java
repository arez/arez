package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnDeactivate;

@ArezComponent
public abstract class MemoizeHasParametersModel
{
  @Memoize
  public int getMyValue( int x )
  {
    return 0;
  }

  @OnDeactivate
  void onMyValueDeactivate()
  {
  }
}
