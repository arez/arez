package com.example.observable_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class GenericObservableRefModel
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

  @Nonnull
  @ObservableRef
  public abstract arez.Observable<MyValue<String>> getMyValueObservable();
}
