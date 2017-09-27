package com.example.on_deactivate;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.OnDeactivate;

@ArezComponent
public class OnDeactivateDuplicateModel
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
