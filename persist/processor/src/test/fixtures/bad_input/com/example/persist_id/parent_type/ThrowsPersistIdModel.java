package com.example.persist_id.parent_type;

import arez.annotations.ArezComponentLike;
import arez.persist.PersistId;
import java.io.IOException;

@ArezComponentLike
abstract class ThrowsPersistIdModel
{
  @PersistId
  int getId()
    throws IOException
  {
    return 0;
  }
}
