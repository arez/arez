package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;

@ArezComponent
abstract class Suppressed1ProtectedAccessOnActivateModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:ProtectedMethod" )
  @OnActivate
  protected void onTimeActivate()
  {
  }
}
