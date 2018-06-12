package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent( deferSchedule = true )
abstract class DeferredButNoAutorunModel
{
  @Observable
  abstract long getField();

  abstract void setField( long field );
}
