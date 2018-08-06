package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent( allowEmpty = true )
abstract class BadName3ReferenceIdModel
{
  @Reference
  abstract MyEntity getMyEntity();

  @ReferenceId
  int blah()
  {
    return 0;
  }

  static class MyEntity
  {
  }
}
