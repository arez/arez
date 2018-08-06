package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent( allowEmpty = true )
abstract class BadNameReferenceIdModel
{
  @Reference
  abstract MyEntity getMyEntity();

  @ReferenceId( name = "-hello" )
  int getMyEntityId()
  {
    return 0;
  }

  static class MyEntity
  {
  }
}
