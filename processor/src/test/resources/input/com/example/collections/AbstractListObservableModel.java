package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.List;

@ArezComponent
public abstract class AbstractListObservableModel
{
  @Observable
  public abstract List<String> getMyValue();

  public abstract void setMyValue( List<String> value );
}
