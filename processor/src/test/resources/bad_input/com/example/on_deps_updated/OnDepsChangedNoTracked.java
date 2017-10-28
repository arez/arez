package com.example.on_deps_updated;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsChanged;

@ArezComponent
public class OnDepsChangedNoTracked
{
  @OnDepsChanged
  void onRenderDepsChanged()
  {
  }
}
