package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;

@ArezComponent
abstract class Suppressed1PublicAccessOnActivateModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:PublicHookMethod" )
  @OnActivate
  public void onTimeActivate()
  {
  }
}
