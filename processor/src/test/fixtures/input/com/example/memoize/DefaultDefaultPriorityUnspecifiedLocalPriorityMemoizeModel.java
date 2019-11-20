package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Priority;

@ArezComponent( defaultPriority = Priority.DEFAULT )
abstract class DefaultDefaultPriorityUnspecifiedLocalPriorityMemoizeModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }
}
