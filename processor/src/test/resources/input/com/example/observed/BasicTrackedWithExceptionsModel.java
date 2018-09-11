package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;
import java.text.ParseException;

@ArezComponent
public abstract class BasicTrackedWithExceptionsModel
{
  @Observed( executor = Executor.APPLICATION )
  public void render()
    throws ParseException
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
