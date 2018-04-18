package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDeactivate;

@ArezComponent
public abstract class OnDeactivateKeepAliveModel
{
  @Computed( keepAlive = true )
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate
  void onMyValueDeactivate()
  {
  }
}
