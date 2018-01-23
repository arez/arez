package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public class DeprecatedObservableModel1
{
  @Deprecated
  @Observable
  public long getTime()
  {
    return 0;
  }

  void setTime( long time )
  {
  }
}
