package com.example.component;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

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
