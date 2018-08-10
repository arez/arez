package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;

@ArezComponent
abstract class MissingReferenceIdReferenceModel
{
  @Reference
  abstract MyEntity getMyEntity();

  static class MyEntity
  {
  }
}
