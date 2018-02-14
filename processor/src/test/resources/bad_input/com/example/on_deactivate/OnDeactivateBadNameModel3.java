package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDeactivate;

@ArezComponent
public abstract class OnDeactivateBadNameModel3
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate( name = "-a-" )
  void foo()
  {
  }
}
