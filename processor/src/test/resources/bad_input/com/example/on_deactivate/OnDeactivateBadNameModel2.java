package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDeactivate;

@ArezComponent
public abstract class OnDeactivateBadNameModel2
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate( name = "abstract" )
  void foo()
  {
  }
}
