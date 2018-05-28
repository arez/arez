package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ComputedNonnullSetModel
{
  @Nonnull
  @Computed
  public Set<String> getMyValue()
  {
    return new HashSet<>();
  }
}
