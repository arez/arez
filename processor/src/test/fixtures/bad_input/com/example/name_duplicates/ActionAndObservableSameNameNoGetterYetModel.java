package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class ActionAndObservableSameNameNoGetterYetModel
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
