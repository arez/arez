package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class MemoizeNonnullMapModel
{
  @NonNull
  @Memoize
  public Map<String, String> getMyValue()
  {
    return new HashMap<>();
  }
}
