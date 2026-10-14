package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnDeactivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class OnDeactivateBadNameModel2
{
  @Memoize
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate( name = "abstract" )
  void foo()
  {
  }
}
