package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent
abstract class ThrowsReferenceIdModel
{
  @Reference
  abstract MyEntity getMyEntity();

  @SuppressWarnings( "RedundantThrows" )
  @ReferenceId
  int getMyEntityId()
    throws Exception
  {
    return 0;
  }

  static class MyEntity
  {
  }
}
