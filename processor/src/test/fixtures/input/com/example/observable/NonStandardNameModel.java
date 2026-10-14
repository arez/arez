package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class NonStandardNameModel
{
  @Observable( name = "time" )
  public abstract long $$getTime();

  @Observable( name = "time" )
  public abstract void setTime$$$$( long $$value );
}
