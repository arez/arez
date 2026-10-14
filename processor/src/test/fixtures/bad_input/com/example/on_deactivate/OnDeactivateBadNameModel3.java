package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnDeactivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class OnDeactivateBadNameModel3
{
  @Memoize
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate( name = "-a-" )
  void foo()
  {
  }
}
