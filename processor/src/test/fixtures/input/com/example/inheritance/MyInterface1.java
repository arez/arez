package com.example.inheritance;

import arez.annotations.Action;
import org.jspecify.annotations.NullMarked;

@NullMarked
interface MyInterface1
{
  @Action
  default void doStuff()
  {
  }
}
