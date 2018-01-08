package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Injectible;

@ArezComponent( inject = Injectible.TRUE )
public class DefaultCtorModel
{
  @Action
  public void myActionStuff()
  {
  }
}
