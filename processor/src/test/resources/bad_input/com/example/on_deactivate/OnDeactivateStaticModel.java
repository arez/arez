package com.example.on_deactivate;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.OnDeactivate;

@ArezComponent
public class OnDeactivateStaticModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate
  static void onMyValueDeactivate()
  {
  }
}
