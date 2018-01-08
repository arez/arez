package com.example.tracked.other;

import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

public class BaseModelProtectedAccess
{
  @Track
  protected void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  protected final void onRenderDepsChanged()
  {
  }
}
