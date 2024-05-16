package com.example.inheritance.interface_inheritance;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
abstract class MyModel
  implements MyInterface
{
  @Memoize
  int myMemoize()
  {
    return 0;
  }
}
