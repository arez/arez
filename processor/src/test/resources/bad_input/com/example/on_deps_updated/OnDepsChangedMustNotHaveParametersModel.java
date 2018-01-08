package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public class OnDepsChangedMustNotHaveParametersModel
{
  @Track
  public void render()
  {
  }

  @OnDepsChanged
  void onRenderDepsChanged( int x )
  {
  }
}
