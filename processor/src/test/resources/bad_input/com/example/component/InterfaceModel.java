package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public interface InterfaceModel
{
  @Observable
  default long getField()
  {
    return 1;
  }

  @Observable
  default void setField( final long field )
  {
  }
}
