package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class MissingReferenceReferenceIdModel
{
  @ReferenceId
  int getMyEntityId()
  {
    return 0;
  }
}
