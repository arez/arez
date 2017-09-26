package com.example.tracked;

import java.text.ParseException;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Tracked;

@Container
public class BasicTrackedWithExceptionsModel
{
  @Tracked
  public void render()
    throws ParseException
  {
  }

  @OnDepsUpdated
  public void onRenderDepsUpdated()
  {
  }
}
