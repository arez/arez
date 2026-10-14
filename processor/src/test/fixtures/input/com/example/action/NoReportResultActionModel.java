package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class NoReportResultActionModel
{
  @Action( reportResult = false )
  public int doStuff()
  {
    return 0;
  }
}
