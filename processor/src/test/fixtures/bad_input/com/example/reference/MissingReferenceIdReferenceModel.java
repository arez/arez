package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class MissingReferenceIdReferenceModel
{
  @Reference
  abstract @Nullable MyEntity getMyEntity();

  @NullMarked
  static class MyEntity
  {
  }
}
