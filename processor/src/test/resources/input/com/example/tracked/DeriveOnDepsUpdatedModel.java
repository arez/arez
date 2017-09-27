package com.example.tracked;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Tracked;

@ArezComponent
public class DeriveOnDepsUpdatedModel
{
  @Tracked
  public void render( final long time, float someOtherParameter )
  {
  }

  public void onRenderDepsUpdated()
  {
  }
}
