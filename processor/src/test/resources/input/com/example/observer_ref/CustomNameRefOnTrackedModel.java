package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public class CustomNameRefOnTrackedModel
{
  @Track
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }

  @ObserverRef( name = "render" )
  Observer observer()
  {
    throw new IllegalStateException();
  }
}
