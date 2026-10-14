package com.example.persist_id.other;

import arez.annotations.ArezComponentLike;
import arez.persist.PersistId;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponentLike
public abstract class BasePackageAccessPersistIdModel
{
  @PersistId
  int getId()
  {
    return 0;
  }
}
