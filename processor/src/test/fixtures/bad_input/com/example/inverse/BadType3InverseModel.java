package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class BadType3InverseModel
{
  @Inverse
  abstract @Nullable String getMyEntity();
}
