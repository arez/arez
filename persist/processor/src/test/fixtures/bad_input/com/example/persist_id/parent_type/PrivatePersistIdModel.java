package com.example.persist_id.parent_type;

import arez.annotations.ArezComponentLike;
import arez.persist.PersistId;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponentLike
abstract class PrivatePersistIdModel
{
  @PersistId
  private int getId()
  {
    return 0;
  }
}
