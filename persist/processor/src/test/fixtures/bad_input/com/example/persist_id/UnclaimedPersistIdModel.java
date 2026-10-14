package com.example.persist_id;

import arez.persist.PersistId;
import org.jspecify.annotations.NullMarked;

@NullMarked
abstract class UnclaimedPersistIdModel
{
  @PersistId
  int getId()
  {
    return 0;
  }
}
