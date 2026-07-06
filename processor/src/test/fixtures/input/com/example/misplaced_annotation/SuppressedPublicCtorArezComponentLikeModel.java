package com.example.misplaced_annotation;

import arez.annotations.ArezComponentLike;
import arez.annotations.SuppressArezWarnings;

@ArezComponentLike
abstract class SuppressedPublicCtorArezComponentLikeModel
{
  @SuppressArezWarnings( "Arez:PublicConstructor" )
  public SuppressedPublicCtorArezComponentLikeModel()
  {
  }
}
