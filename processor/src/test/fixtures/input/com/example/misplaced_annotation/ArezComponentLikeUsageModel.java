package com.example.misplaced_annotation;

import arez.annotations.ArezComponentLike;
import arez.annotations.Action;
import arez.annotations.AutoObserve;
import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponentLike
@SuppressArezWarnings( "Arez:PublicField" )
abstract class ArezComponentLikeUsageModel
{
  @Action
  void perform()
  {
  }

  @AutoObserve
  final @Nullable Object dependency = null;
}
