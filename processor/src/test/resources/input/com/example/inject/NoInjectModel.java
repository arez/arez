package com.example.inject;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Injectible;

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
