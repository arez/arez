package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import java.util.HashSet;
import java.util.Set;

@ArezComponent
public abstract class ComputedSetModel
{
  @Computed
  public Set<String> getMyValue()
  {
    return new HashSet<>();
  }
}
