package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Collection;
import java.util.List;

@ArezComponent
public abstract class ObservableCollectionModel
{
  @Observable
  public Collection<String> getMyValue()
  {
    return null;
  }

  public void setMyValue( final Collection<String> value )
  {
  }
}
