package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class MissingInverseReferenceModel
{
  @Reference( inverse = Feature.ENABLE )
  abstract @Nullable MyEntity getMyEntity();

  @ReferenceId
  int getMyEntityId()
  {
    return 0;
  }

  @NullMarked
  @ArezComponent( allowEmpty = true )
  abstract static class MyEntity
  {
  }
}
