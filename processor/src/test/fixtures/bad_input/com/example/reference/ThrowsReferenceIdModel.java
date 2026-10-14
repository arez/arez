package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ThrowsReferenceIdModel
{
  @Reference
  abstract @Nullable MyEntity getMyEntity();

  @SuppressWarnings( "RedundantThrows" )
  @ReferenceId
  int getMyEntityId()
    throws Exception
  {
    return 0;
  }

  @NullMarked
  static class MyEntity
  {
  }
}
