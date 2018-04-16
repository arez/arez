package com.example.package_access.other;

import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

public abstract class BaseTrackModel
{
  @Track
  void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
