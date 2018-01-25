package com.example.type_access_levels;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class ReduceAccessLevelModel
{
  ReduceAccessLevelModel( int foo )
  {
  }

  @Observable
  public long getTime()
  {
    return 0;
  }

  public void setTime( final long time )
  {
  }
}
