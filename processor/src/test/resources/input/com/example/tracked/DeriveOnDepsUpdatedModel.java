package com.example.tracked;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Track;

@ArezComponent
public class DeriveOnDepsUpdatedModel
{
  @Track
  public void render( final long time, float someOtherParameter )
  {
  }

  public void onRenderDepsUpdated()
  {
  }
}
