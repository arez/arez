package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Collection;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class AbstractNonnullCollectionObservableModel
{
  @Nonnull
  @Observable
  public abstract Collection<String> getMyValue();

  public abstract void setMyValue( Collection<String> value );
}
