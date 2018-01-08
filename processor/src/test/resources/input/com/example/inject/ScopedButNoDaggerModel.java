package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Injectible;
import javax.inject.Singleton;

@Singleton
@ArezComponent( dagger = Injectible.FALSE )
public class ScopedButNoDaggerModel
{
  public ScopedButNoDaggerModel()
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
