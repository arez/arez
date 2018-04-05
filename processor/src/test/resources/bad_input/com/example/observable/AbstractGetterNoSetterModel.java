package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class AbstractGetterNoSetterModel
{
  @Observable( expectSetter = false )
  public abstract long getField();
}
