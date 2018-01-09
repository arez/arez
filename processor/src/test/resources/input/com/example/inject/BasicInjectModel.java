package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Injectible;

@ArezComponent( inject = Injectible.ENABLE )
public class BasicInjectModel
{
  public BasicInjectModel()
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
