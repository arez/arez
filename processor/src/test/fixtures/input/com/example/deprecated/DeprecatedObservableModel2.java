package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class DeprecatedObservableModel2
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
