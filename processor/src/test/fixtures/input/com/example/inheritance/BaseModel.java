package com.example.inheritance;

import arez.annotations.Memoize;

class BaseModel
  implements MyInterface1
{
  @Memoize
  protected int myMemoize()
  {
    return 0;
  }
}
