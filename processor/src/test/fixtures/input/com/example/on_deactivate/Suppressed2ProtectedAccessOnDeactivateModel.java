package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnDeactivate;
import arez.annotations.SuppressArezWarnings;

@ArezComponent
abstract class Suppressed2ProtectedAccessOnDeactivateModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:ProtectedHookMethod" )
  @OnDeactivate
  protected void onTimeDeactivate()
  {
  }
}
