package com.example.name_duplicates;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

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
