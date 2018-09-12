package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observed;

@ArezComponent
public abstract class ReportParametersArezExecutorModel
{
  @Observed( reportParameters = false, executor = Executor.AREZ )
  void doStuff()
  {
  }
}
