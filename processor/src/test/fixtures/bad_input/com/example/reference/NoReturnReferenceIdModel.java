package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class NoReturnReferenceIdModel
{
  @Reference
  abstract @Nullable MyEntity getMyEntity();

  @ReferenceId
  void getMyEntityId()
  {
  }

  @NullMarked
  static class MyEntity
  {
  }
}
