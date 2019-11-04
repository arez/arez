package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Map;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class AbstractNonnullMapObservableModel
{
  @Nonnull
  @Observable
  public abstract Map<String, String> getMyValue();

  public abstract void setMyValue( Map<String, String> value );
}
