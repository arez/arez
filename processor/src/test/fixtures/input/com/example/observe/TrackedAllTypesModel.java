package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import java.text.ParseException;

@ArezComponent
abstract class TrackedAllTypesModel
{
  @Observe( executor = Executor.EXTERNAL )
  public void render1()
  {
  }

  @Observe( executor = Executor.EXTERNAL )
  public void render2()
    throws ParseException
  {
  }

  @Observe( executor = Executor.EXTERNAL )
  int render3()
  {
    return 0;
  }

  @Observe( executor = Executor.EXTERNAL )
  int render4()
    throws ParseException
  {
    return 0;
  }

  @OnDepsChange
  void onRender1DepsChange()
  {
  }

  @OnDepsChange
  void onRender2DepsChange()
  {
  }

  @OnDepsChange
  void onRender3DepsChange()
  {
  }

  @OnDepsChange
  void onRender4DepsChange()
  {
  }
}
