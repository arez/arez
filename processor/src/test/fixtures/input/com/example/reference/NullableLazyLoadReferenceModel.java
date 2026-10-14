package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.LinkType;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class NullableLazyLoadReferenceModel
{
  @Reference( load = LinkType.LAZY )
  abstract @Nullable MyEntity getMyEntity();

  @ReferenceId
  @Nullable
  Integer getMyEntityId()
  {
    return 0;
  }

  @NullMarked
  static class MyEntity
  {
  }
}
