package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class DeprecatedObservableModel1
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
