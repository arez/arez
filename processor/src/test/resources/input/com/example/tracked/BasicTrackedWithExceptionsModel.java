package com.example.tracked;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;
import java.text.ParseException;

@ArezComponent
public abstract class BasicTrackedWithExceptionsModel
{
  @Track
  public void render()
    throws ParseException
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
