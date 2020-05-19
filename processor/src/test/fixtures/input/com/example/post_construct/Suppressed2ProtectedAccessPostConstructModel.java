package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PostConstruct;
import arez.annotations.SuppressArezWarnings;

@ArezComponent
abstract class Suppressed2ProtectedAccessPostConstructModel
{
  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:ProtectedMethod" )
  @PostConstruct
  protected void postConstruct()
  {
  }

  @Memoize
  public int someValue()
  {
    return 0;
  }
}
