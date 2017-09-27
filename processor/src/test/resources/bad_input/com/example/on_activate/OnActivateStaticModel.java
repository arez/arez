package com.example.on_activate;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.OnActivate;

@ArezComponent
public class OnActivateStaticModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnActivate
  static void onMyValueActivate()
  {
  }
}
