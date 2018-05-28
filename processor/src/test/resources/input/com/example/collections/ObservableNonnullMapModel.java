package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ObservableNonnullMapModel
{
  @Nonnull
  @Observable
  public Map<String, String> getMyValue()
  {
    return new HashMap<>();
  }

  public void setMyValue( final Map<String, String> value )
  {
  }
}
