package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent
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
