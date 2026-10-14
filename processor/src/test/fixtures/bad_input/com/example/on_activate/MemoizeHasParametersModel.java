package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class MemoizeHasParametersModel
{
  @Memoize
  public int getMyValue( int i )
  {
    return 0;
  }

  @OnActivate
  final void onMyValueActivate()
  {
  }
}
