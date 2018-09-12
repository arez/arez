package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent( deferSchedule = true )
abstract class DeferredButNoObservedModel
{
  @Observable
  abstract long getField();

  abstract void setField( long field );
}
