package com.example.persist_id.parent_type;

import arez.annotations.ActAsComponent;
import arez.persist.PersistId;
import java.io.IOException;

@ActAsComponent
abstract class ThrowsPersistIdModel
{
  @PersistId
  int getId()
    throws IOException
  {
    return 0;
  }
}
