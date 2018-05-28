package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import java.util.Collection;
import java.util.HashSet;

@ArezComponent
public abstract class ComputedCollectionModel
{
  @Computed
  public Collection<String> getMyValue()
  {
    return new HashSet<>();
  }
}
