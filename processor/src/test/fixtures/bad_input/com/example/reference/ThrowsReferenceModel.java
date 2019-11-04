package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent
abstract class ThrowsReferenceModel
{
  @SuppressWarnings( { "RedundantThrows", "RedundantSuppression" } )
  @Reference
  abstract MyEntity getMyEntity()
    throws Exception;

  @ReferenceId
  int getMyEntityId()
  {
    return 0;
  }

  static class MyEntity
  {
  }
}
