package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class BadName3ReferenceIdModel
{
  @Reference
  abstract @Nullable MyEntity getMyEntity();

  @ReferenceId
  int blah()
  {
    return 0;
  }

  @NullMarked
  static class MyEntity
  {
  }
}
