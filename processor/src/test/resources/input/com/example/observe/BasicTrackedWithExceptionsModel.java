package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChanged;
import java.text.ParseException;

@ArezComponent
public abstract class BasicTrackedWithExceptionsModel
{
  @Observe( executor = Executor.APPLICATION )
  public void render()
    throws ParseException
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
