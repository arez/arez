package com.example.persist_id.parent_type;

import arez.annotations.ArezComponentLike;
import arez.persist.PersistId;
import java.io.IOException;

@ArezComponentLike
abstract class ParameterizedPersistIdModel
{
  @PersistId
  int getId(int myParam)
  {
    return 0;
  }
}
