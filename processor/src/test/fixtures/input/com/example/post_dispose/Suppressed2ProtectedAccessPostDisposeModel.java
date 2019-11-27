package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;
import arez.annotations.SuppressArezWarnings;

@ArezComponent( allowEmpty = true )
abstract class Suppressed2ProtectedAccessPostDisposeModel
{
  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:ProtectedLifecycleMethod" )
  @PostDispose
  protected void postDispose()
  {
  }
}
