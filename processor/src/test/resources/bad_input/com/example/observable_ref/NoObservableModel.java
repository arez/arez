package com.example.observable_ref;

import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ObservableRef;

@ArezComponent
public class NoObservableModel
{
  @Nonnull
  @ObservableRef
  public org.realityforge.arez.Observable getTimeObservable()
  {
    throw new IllegalStateException();
  }
}
