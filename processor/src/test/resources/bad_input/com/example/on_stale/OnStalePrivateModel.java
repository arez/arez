package com.example.on_stale;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.OnStale;

@ArezComponent
public class OnStalePrivateModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnStale
  private void onMyValueStale()
  {
  }
}
