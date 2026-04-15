package com.example.misplaced_annotation;

import arez.annotations.ArezComponentLike;
import arez.annotations.Action;
import arez.annotations.AutoObserve;
import arez.annotations.SuppressArezWarnings;

@ArezComponentLike
@SuppressArezWarnings( "Arez:PublicField" )
abstract class ArezComponentLikeUsageModel
{
  @Action
  void perform()
  {
  }

  @AutoObserve
  final Object dependency = null;
}
