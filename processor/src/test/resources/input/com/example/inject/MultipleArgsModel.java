package com.example.inject;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Injectible;

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
