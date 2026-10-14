package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ProtectedAccessOnActivateModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  @OnActivate
  protected void onTimeActivate()
  {
  }
}
