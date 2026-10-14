package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PostConstruct;
import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class Suppressed2PublicAccessPostConstructModel
{
  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:PublicLifecycleMethod" )
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
