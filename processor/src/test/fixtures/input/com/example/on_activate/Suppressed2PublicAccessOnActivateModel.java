package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class Suppressed2PublicAccessOnActivateModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:PublicHookMethod" )
  @OnActivate
  public void onTimeActivate()
  {
  }
}
