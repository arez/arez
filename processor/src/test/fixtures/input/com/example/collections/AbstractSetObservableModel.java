package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Set;

@ArezComponent
public abstract class AbstractSetObservableModel
{
  @Observable
  public abstract Set<String> getMyValue();

  public abstract void setMyValue( Set<String> value );
}
