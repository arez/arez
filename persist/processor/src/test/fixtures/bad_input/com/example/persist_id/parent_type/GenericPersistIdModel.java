package com.example.persist_id.parent_type;

import arez.annotations.ActAsComponent;
import arez.persist.PersistId;

@ActAsComponent
abstract class GenericPersistIdModel
{
  @PersistId
  <T> T getId()
  {
    return null;
  }
}
