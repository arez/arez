package com.example.persist_id.parent_type;

import arez.annotations.ArezComponentLike;
import arez.persist.PersistId;

@ArezComponentLike
abstract class ProtectedPersistIdModel
{
  @PersistId
  protected int getId()
  {
    return 0;
  }
}
