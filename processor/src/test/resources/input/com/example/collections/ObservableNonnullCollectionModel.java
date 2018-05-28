package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ObservableNonnullCollectionModel
{
  @Nonnull
  @Observable
  public Collection<String> getMyValue()
  {
    return new ArrayList<>();
  }

  public void setMyValue( final Collection<String> value )
  {
  }
}
