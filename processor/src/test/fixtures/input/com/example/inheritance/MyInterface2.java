package com.example.inheritance;

import arez.annotations.Action;
import org.jspecify.annotations.NullMarked;

@NullMarked
interface MyInterface2
{
  @Action
  default void doOtherStuff()
  {
  }
}
