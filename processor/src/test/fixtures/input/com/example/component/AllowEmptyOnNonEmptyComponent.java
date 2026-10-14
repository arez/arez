package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class AllowEmptyOnNonEmptyComponent
{
  @Action
  void foo()
  {
  }
}
