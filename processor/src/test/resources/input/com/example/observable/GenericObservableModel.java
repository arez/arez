package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class GenericObservableModel
{
  public interface MyValue<T>
  {
  }

  @Observable
  public MyValue<String> getMyValue()
  {
    return null;
  }

  public void setMyValue( final MyValue<String> time )
  {
  }
}
