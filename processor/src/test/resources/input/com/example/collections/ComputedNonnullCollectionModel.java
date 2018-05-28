package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import java.util.Collection;
import java.util.HashSet;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ComputedNonnullCollectionModel
{
  @Nonnull
  @Computed
  public Collection<String> getMyValue()
  {
    return new HashSet<>();
  }
}
