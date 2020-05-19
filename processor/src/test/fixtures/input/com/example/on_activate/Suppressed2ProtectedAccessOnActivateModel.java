package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.annotations.SuppressArezWarnings;

@ArezComponent
abstract class Suppressed2ProtectedAccessOnActivateModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:ProtectedMethod" )
  @OnActivate
  protected void onTimeActivate()
  {
  }
}
