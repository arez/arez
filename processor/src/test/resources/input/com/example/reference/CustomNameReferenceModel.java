package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent
abstract class CustomNameReferenceModel
{
  @Reference( name = "Blah" )
  abstract MyEntity getMyEntity();

  @ReferenceId( name = "Blah" )
  int getMyEntityId()
  {
    return 0;
  }

  static class MyEntity
  {
  }
}
