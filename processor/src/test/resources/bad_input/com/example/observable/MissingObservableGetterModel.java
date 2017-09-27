package com.example.observable;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

@ArezComponent
public class MissingObservableGetterModel
{
  @Observable
  public void setField( final long field )
  {
  }
}
