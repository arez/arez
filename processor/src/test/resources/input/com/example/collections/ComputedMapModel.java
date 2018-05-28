package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import java.util.HashMap;
import java.util.Map;

@ArezComponent
public abstract class ComputedMapModel
{
  @Computed
  public Map<String, String> getMyValue()
  {
    return new HashMap<>();
  }
}
