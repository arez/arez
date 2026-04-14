package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public abstract class ReportParametersWithNoParametersActionModel
{
  @Action( reportParameters = false )
  public void doStuff()
  {
  }
}
