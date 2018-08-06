package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.ReferenceId;

@ArezComponent( allowEmpty = true )
abstract class MissingReferenceReferenceIdModel
{
  @ReferenceId
  int getMyEntityId()
  {
    return 0;
  }
}
