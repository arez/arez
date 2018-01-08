package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Injectible;

@ArezComponent( inject = Injectible.FALSE )
public class NoInjectModel
{
  public NoInjectModel()
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
