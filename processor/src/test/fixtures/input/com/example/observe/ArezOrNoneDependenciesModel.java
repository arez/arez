package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Observe;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ArezOrNoneDependenciesModel
{
  @Observe( depType = DepType.AREZ_OR_NONE )
  void render()
  {
  }
}
