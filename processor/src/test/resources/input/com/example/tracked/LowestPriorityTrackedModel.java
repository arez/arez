package com.example.tracked;

import arez.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public abstract class LowestPriorityTrackedModel
{
  @Track( priority = Priority.LOWEST )
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
