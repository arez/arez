package com.example.tracked;

import java.text.ParseException;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Tracked;

@ArezComponent
public class TrackedAllTypesModel
{
  @Tracked
  public void render1()
  {
  }

  @Tracked
  public void render2()
    throws ParseException
  {
  }

  @Tracked
  protected int render3()
  {
    return 0;
  }

  @Tracked
  int render4()
    throws ParseException
  {
    return 0;
  }

  @OnDepsUpdated
  public void onRender1DepsUpdated()
  {
  }

  @OnDepsUpdated
  void onRender2DepsUpdated()
  {
  }

  @OnDepsUpdated
  protected void onRender3DepsUpdated()
  {
  }

  @OnDepsUpdated
  public void onRender4DepsUpdated()
  {
  }
}
