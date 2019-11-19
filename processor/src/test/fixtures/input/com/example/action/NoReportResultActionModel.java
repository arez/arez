package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
abstract class NoReportResultActionModel
{
  @Action( reportResult = false )
  public int doStuff()
  {
    return 0;
  }
}
