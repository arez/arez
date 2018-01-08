package com.example.type_access_levels;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

@ArezComponent
public class ReduceAccessLevelModel
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
