package com.example.inheritance;

import org.realityforge.arez.annotations.Action;

interface MyInterface1
{
  @Action
  default void doStuff()
  {
  }
}
