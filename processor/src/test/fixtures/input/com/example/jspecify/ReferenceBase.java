package com.example.jspecify;

import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
abstract class ReferenceBase<T extends @Nullable Object>
{
  private final T _id;

  ReferenceBase( final T id )
  {
    _id = id;
  }

  @ReferenceId
  T getEntityId()
  {
    return _id;
  }

  @Reference
  abstract @Nullable Entity getEntity();

  static class Entity
  {
  }
}
