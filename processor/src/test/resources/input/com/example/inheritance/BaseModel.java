package com.example.inheritance;

import arez.annotations.Computed;

class BaseModel
  implements MyInterface1
{
  @Computed
  protected int myComputed()
  {
    return 0;
  }
}
