package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.HashMap;
import java.util.Map;

@ArezComponent
public abstract class ObservableMapModel
{
  @Observable
  public Map<String, String> getMyValue()
  {
    return new HashMap<>();
  }

  public void setMyValue( final Map<String, String> value )
  {
  }
}
