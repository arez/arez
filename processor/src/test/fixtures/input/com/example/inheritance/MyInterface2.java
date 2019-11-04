package com.example.inheritance;

import arez.annotations.Action;

interface MyInterface2
{
  @Action
  default void doOtherStuff()
  {
  }
}
