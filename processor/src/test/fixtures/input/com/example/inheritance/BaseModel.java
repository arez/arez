package com.example.inheritance;

import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;

@NullMarked
class BaseModel
  implements MyInterface1
{
  @Memoize
  protected int myMemoize()
  {
    return 0;
  }
}
