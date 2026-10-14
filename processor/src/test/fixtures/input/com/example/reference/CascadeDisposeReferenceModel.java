package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class CascadeDisposeReferenceModel
{
  @Reference
  @CascadeDispose
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
