package com.example.on_deactivate.other;

import arez.annotations.Memoize;
import arez.annotations.OnDeactivate;

public abstract class BaseProtectedAccessOnDeactivateModel
{
  @Memoize
  protected long getTime()
  {
    return 0;
  }

  @OnDeactivate
  protected void onTimeDeactivate()
  {
  }
}
