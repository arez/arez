package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import java.text.ParseException;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class BasicTrackedWithExceptionsModel
{
  @Observe( executor = Executor.EXTERNAL )
  public void render()
    throws ParseException
  {
  }

  @OnDepsChange
  void onRenderDepsChange()
  {
  }
}
