package com.example.tracked;

import java.text.ParseException;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsChanged;
import org.realityforge.arez.annotations.Track;

@ArezComponent
public class BasicTrackedWithExceptionsModel
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
