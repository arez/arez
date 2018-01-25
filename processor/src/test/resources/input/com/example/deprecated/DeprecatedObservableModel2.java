package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class DeprecatedObservableModel2
{
  @Observable
  public long getTime()
  {
    return 0;
  }

  @Deprecated
  void setTime( long time )
  {
  }
}
