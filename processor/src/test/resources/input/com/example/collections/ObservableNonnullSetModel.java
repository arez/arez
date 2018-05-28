package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ObservableNonnullSetModel
{
  @Nonnull
  @Observable
  public Set<String> getMyValue()
  {
    return new HashSet<>();
  }

  public void setMyValue( final Set<String> value )
  {
  }
}
