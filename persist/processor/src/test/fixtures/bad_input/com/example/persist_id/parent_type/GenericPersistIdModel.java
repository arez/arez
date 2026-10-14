package com.example.persist_id.parent_type;

import arez.annotations.ArezComponentLike;
import arez.persist.PersistId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponentLike
abstract class GenericPersistIdModel
{
  @PersistId
  <T extends @Nullable Object> @Nullable T getId()
  {
    return null;
  }
}
