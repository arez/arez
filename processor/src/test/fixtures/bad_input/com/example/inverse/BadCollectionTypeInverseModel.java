package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import java.util.Collection;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class BadCollectionTypeInverseModel
{
  @Inverse
  abstract @Nullable Collection<String> getMyEntity();
}
