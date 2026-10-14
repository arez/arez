package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ConcreteReferenceModel
{
  @Reference
  @Nullable MyEntity getMyEntity()
  {
    return null;
  }

  @ReferenceId
  int getMyEntityId()
  {
    return 0;
  }

  @NullMarked
  static class MyEntity
  {
  }
}
