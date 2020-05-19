package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnDeactivate;

@ArezComponent
abstract class Suppressed1ProtectedAccessOnDeactivateModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:ProtectedMethod" )
  @OnDeactivate
  protected void onTimeDeactivate()
  {
  }
}
