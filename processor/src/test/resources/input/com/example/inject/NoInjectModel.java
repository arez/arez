package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;

@ArezComponent( inject = InjectMode.NONE )
public abstract class NoInjectModel
{
  NoInjectModel()
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
