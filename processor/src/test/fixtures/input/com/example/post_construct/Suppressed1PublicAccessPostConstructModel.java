package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PostConstruct;

@ArezComponent
abstract class Suppressed1PublicAccessPostConstructModel
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:PublicLifecycleMethod" )
  @PostConstruct
  public void postConstruct()
  {
  }

  @Memoize
  public int someValue()
  {
    return 0;
  }
}
