package com.example.tracked;

import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Tracked;

@Container
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
