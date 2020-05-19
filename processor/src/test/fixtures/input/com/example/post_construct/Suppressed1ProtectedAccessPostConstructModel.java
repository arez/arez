package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PostConstruct;

@ArezComponent
abstract class Suppressed1ProtectedAccessPostConstructModel
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:ProtectedMethod" )
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
