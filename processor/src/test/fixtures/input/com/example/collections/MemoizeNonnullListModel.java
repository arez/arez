package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class MemoizeNonnullListModel
{
  @NonNull
  @Memoize
  public List<String> getMyValue()
  {
    return new ArrayList<>();
  }
}
