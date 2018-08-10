package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent
abstract class BadName2ReferenceIdModel
{
  @Reference
  abstract MyEntity getMyEntity();

  @ReferenceId( name = "long" )
  int getMyEntityId()
  {
    return 0;
  }

  static class MyEntity
  {
  }
}
