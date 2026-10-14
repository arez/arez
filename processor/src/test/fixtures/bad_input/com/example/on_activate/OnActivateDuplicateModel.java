package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class OnActivateDuplicateModel
{
  @Memoize
  public int getMyValue()
  {
    return 0;
  }

  @OnActivate( name = "myValue" )
  void foo()
  {
  }

  @OnActivate
  void onMyValueActivate()
  {
  }
}
