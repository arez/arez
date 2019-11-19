package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
abstract class NoReportResultModel
{
  @Memoize( reportResult = false )
  public long getTime()
  {
    return 0;
  }
}
