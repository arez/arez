package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ComputedNonnullMapModel
{
  @Nonnull
  @Computed
  public Map<String, String> getMyValue()
  {
    return new HashMap<>();
  }
}
