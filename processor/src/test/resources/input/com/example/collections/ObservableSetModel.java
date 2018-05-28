package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ObservableSetModel
{
  @Observable
  public Set<String> getMyValue()
  {
    return new HashSet<>();
  }

  public void setMyValue( final Set<String> value )
  {
  }
}
