package com.example.inheritance;

import arez.annotations.Action;

interface MyInterface1
{
  @Action
  default void doStuff()
  {
  }
}
