package com.example.inheritance;

import org.realityforge.arez.annotations.Action;

interface MyInterface2
{
  @Action
  default void doOtherStuff()
  {
  }
}
