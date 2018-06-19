package com.example.tracked;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Priority;
import arez.annotations.Track;

@ArezComponent
public abstract class NormalPriorityTrackedModel
{
  @Track( priority = Priority.NORMAL )
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
