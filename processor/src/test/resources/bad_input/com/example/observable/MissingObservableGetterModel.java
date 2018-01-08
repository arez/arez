package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public class MissingObservableGetterModel
{
  @Observable
  public void setField( final long field )
  {
  }
}
