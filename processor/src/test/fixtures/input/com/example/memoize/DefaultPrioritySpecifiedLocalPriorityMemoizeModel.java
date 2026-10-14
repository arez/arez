package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Priority;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( defaultPriority = Priority.HIGH )
abstract class DefaultPrioritySpecifiedLocalPriorityMemoizeModel
{
  @Memoize( priority = Priority.NORMAL )
  public long getTime()
  {
    return 0;
  }
}
