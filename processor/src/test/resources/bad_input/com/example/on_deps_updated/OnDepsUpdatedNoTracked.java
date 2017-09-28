package com.example.on_deps_updated;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsUpdated;

@ArezComponent
public class OnDepsUpdatedNoTracked
{
  @OnDepsUpdated
  void onRenderDepsUpdated()
  {
  }
}
