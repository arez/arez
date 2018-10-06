package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChanged;
import java.text.ParseException;

@ArezComponent
public abstract class TrackedAllTypesModel
{
  @Observe( executor = Executor.APPLICATION )
  public void render1()
  {
  }

  @Observe( executor = Executor.APPLICATION )
  public void render2()
    throws ParseException
  {
  }

  @Observe( executor = Executor.APPLICATION )
  protected int render3()
  {
    return 0;
  }

  @Observe( executor = Executor.APPLICATION )
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
