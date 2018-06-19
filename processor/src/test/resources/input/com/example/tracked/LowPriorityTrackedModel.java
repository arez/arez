package com.example.tracked;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Priority;
import arez.annotations.Track;

@ArezComponent
public abstract class LowPriorityTrackedModel
{
  @Track( priority = Priority.LOW )
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
