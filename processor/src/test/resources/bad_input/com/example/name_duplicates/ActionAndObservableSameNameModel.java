package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class ActionAndObservableSameNameModel
{
  @Action( name = "x" )
  public long m1()
  {
    return 22;
  }

  @Observable( name = "x" )
  public long getTime()
  {
    return 0;
  }

  @Observable( name = "x" )
  public void setTime( final long time )
  {
  }
}
