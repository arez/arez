package com.example.persist_id.other;

import arez.annotations.ArezComponentLike;
import arez.persist.PersistId;

@ArezComponentLike
public abstract class BasePackageAccessPersistIdModel
{
  @PersistId
  int getId()
  {
    return 0;
  }
}
