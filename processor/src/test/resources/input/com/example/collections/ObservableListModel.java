package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.List;

@ArezComponent
public abstract class ObservableListModel
{
  @Observable
  public List<String> getMyValue()
  {
    return null;
  }

  public void setMyValue( final List<String> value )
  {
  }
}
