package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;

@ArezComponent
public abstract class ReportResultArezExecutorModel
{
  @Observe( reportResult = false, executor = Executor.AREZ )
  void doStuff()
  {
  }
}
