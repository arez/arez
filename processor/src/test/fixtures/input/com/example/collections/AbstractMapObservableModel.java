package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Map;

@ArezComponent
public abstract class AbstractMapObservableModel
{
  @Observable
  public abstract Map<String, String> getMyValue();

  public abstract void setMyValue( Map<String, String> value );
}
