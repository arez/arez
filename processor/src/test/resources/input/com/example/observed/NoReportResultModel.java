package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class NoReportResultModel
{
  @Observe( executor = Executor.APPLICATION, reportResult = false )
  public int render()
  {
    return 0;
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
