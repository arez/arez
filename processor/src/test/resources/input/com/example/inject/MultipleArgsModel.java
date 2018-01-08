package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Injectible;

@ArezComponent( inject = Injectible.TRUE )
public class MultipleArgsModel
{
  public MultipleArgsModel( int i, String foo )
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
