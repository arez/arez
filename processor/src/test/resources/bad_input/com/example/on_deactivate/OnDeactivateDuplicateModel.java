package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDeactivate;

@ArezComponent
public abstract class OnDeactivateDuplicateModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate( name = "myValue" )
  void foo()
  {
  }

  @OnDeactivate
  void onMyValueDeactivate()
  {
  }
}
