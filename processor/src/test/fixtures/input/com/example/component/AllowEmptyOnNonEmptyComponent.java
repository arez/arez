package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent( allowEmpty = true )
abstract class AllowEmptyOnNonEmptyComponent
{
  @Action
  void foo()
  {
  }
}
