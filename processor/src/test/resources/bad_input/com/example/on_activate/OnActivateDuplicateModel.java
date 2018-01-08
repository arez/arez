package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnActivate;

@ArezComponent
public class OnActivateDuplicateModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnActivate( name = "myValue" )
  void foo()
  {
  }

  @OnActivate
  void onMyValueActivate()
  {
  }
}
