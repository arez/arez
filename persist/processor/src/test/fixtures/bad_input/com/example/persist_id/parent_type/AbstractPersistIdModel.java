package com.example.persist_id.parent_type;

import arez.annotations.ActAsComponent;
import arez.persist.PersistId;

@ActAsComponent
abstract class AbstractPersistIdModel
{
  @PersistId
  abstract int getId();
}
