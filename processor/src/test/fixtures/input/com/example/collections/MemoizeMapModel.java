package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.HashMap;
import java.util.Map;

@ArezComponent
public abstract class MemoizeMapModel
{
  @Memoize
  public Map<String, String> getMyValue()
  {
    return new HashMap<>();
  }
}
