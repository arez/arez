package com.example.tracked;

import java.text.ParseException;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsChanged;
import org.realityforge.arez.annotations.Track;

@ArezComponent
public class TrackedAllTypesModel
{
  @Track
  public void render1()
  {
  }

  @Track
  public void render2()
    throws ParseException
  {
  }

  @Track
  protected int render3()
  {
    return 0;
  }

  @Track
  int render4()
    throws ParseException
  {
    return 0;
  }

  @OnDepsChanged
  public void onRender1DepsChanged()
  {
  }

  @OnDepsChanged
  void onRender2DepsChanged()
  {
  }

  @OnDepsChanged
  protected void onRender3DepsChanged()
  {
  }

  @OnDepsChanged
  public void onRender4DepsChanged()
  {
  }
}
