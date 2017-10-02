package com.example.tracked;

import java.text.ParseException;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Track;

@ArezComponent
public class BasicTrackedWithExceptionsModel
{
  @Track
  public void render()
    throws ParseException
  {
  }

  @OnDepsUpdated
  public void onRenderDepsUpdated()
  {
  }
}
