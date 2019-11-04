package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
public abstract class NoReportResultModel
{
  @Observe( executor = Executor.EXTERNAL, reportResult = false )
  public int render()
  {
    return 0;
  }

  @OnDepsChange
  public void onRenderDepsChange()
  {
  }
}
