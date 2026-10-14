package com.example.jspecify;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class InheritedNullableReferenceModel extends ReferenceBase<@Nullable Integer>
{
  InheritedNullableReferenceModel()
  {
    super( null );
  }
}
