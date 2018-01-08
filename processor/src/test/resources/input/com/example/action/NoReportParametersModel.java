package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public class NoReportParametersModel
{
  @Action( reportParameters = false )
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
