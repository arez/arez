package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public class ActionAndObservableSameNameNoGetterYetModel
{
  @Observable( name = "x" )
  public void setTime( final long time )
  {
  }

  @Action( name = "x" )
  public long m1()
  {
    return 22;
  }

  public long getTime()
  {
    return 0;
  }
}
