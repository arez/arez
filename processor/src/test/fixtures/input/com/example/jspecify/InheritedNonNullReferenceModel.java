package com.example.jspecify;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class InheritedNonNullReferenceModel extends ReferenceBase<Integer>
{
  InheritedNonNullReferenceModel()
  {
    super( 1 );
  }
}
