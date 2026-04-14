package com.example.persist_id.parent_type;

import arez.annotations.ActAsComponent;
import arez.persist.PersistId;

@ActAsComponent
abstract class StaticPersistIdModel
{
  @PersistId
  static int getId()
  {
    return 0;
  }
}
