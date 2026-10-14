package com.example.on_activate.other;

import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseProtectedAccessOnActivateModel
{
  @Memoize
  protected long getTime()
  {
    return 0;
  }

  @OnActivate
  protected void onTimeActivate()
  {
  }
}
