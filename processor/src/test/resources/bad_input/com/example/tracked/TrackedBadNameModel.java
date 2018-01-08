package com.example.tracked;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public class TrackedBadNameModel
{
  @Track(name = "-ace")
  static void render()
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
