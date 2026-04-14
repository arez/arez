package com.example.persist_id.other;

import arez.annotations.ActAsComponent;
import arez.persist.PersistId;

@ActAsComponent
public abstract class BasePackageAccessPersistIdModel
{
  @PersistId
  int getId()
  {
    return 0;
  }
}
