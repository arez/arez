package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDeactivate;

@ArezComponent
public class OnDeactivateParametersModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate
  void onMyValueDeactivate( int x )
  {
  }
}
