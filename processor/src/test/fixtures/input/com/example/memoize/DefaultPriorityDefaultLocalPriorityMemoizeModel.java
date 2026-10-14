package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Priority;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( defaultPriority = Priority.HIGH )
abstract class DefaultPriorityDefaultLocalPriorityMemoizeModel
{
  @Memoize( priority = Priority.DEFAULT )
  public long getTime()
  {
    return 0;
  }
}
