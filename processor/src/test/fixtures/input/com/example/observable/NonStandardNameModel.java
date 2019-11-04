package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class NonStandardNameModel
{
  @Observable( name = "time" )
  public abstract long $$getTime();

  @Observable( name = "time" )
  public abstract void setTime$$$$( long $$value );
}
