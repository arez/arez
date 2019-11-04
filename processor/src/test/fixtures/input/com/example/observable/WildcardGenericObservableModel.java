package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class WildcardGenericObservableModel<T>
{
  public interface MyValue<T>
  {
  }

  @Observable
  public MyValue<T> getMyValue()
  {
    return null;
  }

  public void setMyValue( final MyValue<T> time )
  {
  }
}
