package com.example.persist_id.parent_type;

import arez.annotations.ArezComponentLike;
import arez.persist.PersistId;

@ArezComponentLike
abstract class PrivatePersistIdModel
{
  @PersistId
  private int getId()
  {
    return 0;
  }
}
