package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;
import java.text.ParseException;

@ArezComponent
public abstract class TrackedAllTypesModel
{
  @Observed( executor = Executor.APPLICATION )
  public void render1()
  {
  }

  @Observed( executor = Executor.APPLICATION )
  public void render2()
    throws ParseException
  {
  }

  @Observed( executor = Executor.APPLICATION )
  protected int render3()
  {
    return 0;
  }

  @Observed( executor = Executor.APPLICATION )
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
