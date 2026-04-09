package com.example.persist_id;

import arez.persist.PersistId;

abstract class UnclaimedPersistIdModel
{
  @PersistId
  int getId()
  {
    return 0;
  }
}
