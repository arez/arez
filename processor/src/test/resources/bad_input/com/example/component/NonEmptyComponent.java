package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent( allowEmpty = true )
public abstract class NonEmptyComponent
{
  @Action
  void foo()
  {
  }
}
