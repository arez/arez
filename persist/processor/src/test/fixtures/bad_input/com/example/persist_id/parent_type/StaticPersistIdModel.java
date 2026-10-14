package com.example.persist_id.parent_type;

import arez.annotations.ArezComponentLike;
import arez.persist.PersistId;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponentLike
abstract class StaticPersistIdModel
{
  @PersistId
  static int getId()
  {
    return 0;
  }
}
