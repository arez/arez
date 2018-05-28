package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Set;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class AbstractNonnullSetObservableModel
{
  @Nonnull
  @Observable
  public abstract Set<String> getMyValue();

  public abstract void setMyValue( Set<String> value );
}
