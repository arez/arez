package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@SuppressWarnings( "rawtypes" )
@ArezComponent
abstract class RawObservableModel
{
  public interface MyValue<T>
  {
  }

  @Observable
  public abstract MyValue getMyValue();

  public abstract void setMyValue( final MyValue time );
}
