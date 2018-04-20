package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;

@ArezComponent
public abstract class InitializerParametersDisagreeModel
{
  @Observable( initializer = Feature.ENABLE )
  public abstract long getField();

  @Observable( initializer = Feature.DISABLE )
  public abstract void setField( long field );
}
