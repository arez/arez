package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;

@ArezComponent
public abstract class ReportParametersArezExecutorModel
{
  @Observe( reportParameters = false, executor = Executor.AREZ )
  void doStuff()
  {
  }
}
