package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Collection;

@ArezComponent
public abstract class AbstractCollectionObservableModel
{
  @Observable
  public abstract Collection<String> getMyValue();

  public abstract void setMyValue( Collection<String> value );
}
