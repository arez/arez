package com.example.inject;

import javax.inject.Singleton;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Injectible;

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
