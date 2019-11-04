package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.List;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class AbstractNonnullListObservableModel
{
  @Nonnull
  @Observable
  public abstract List<String> getMyValue();

  public abstract void setMyValue( List<String> value );
}
