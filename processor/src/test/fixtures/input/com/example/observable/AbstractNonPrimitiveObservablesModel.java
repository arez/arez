package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Date;

@ArezComponent
public abstract class AbstractNonPrimitiveObservablesModel
{
  @Observable
  public abstract Date getTime();

  public abstract void setTime( Date value );
}
