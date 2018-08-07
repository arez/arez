package com.example.entity_locator;

import arez.Locator;
import arez.annotations.ArezComponent;
import arez.annotations.LocatorRef;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent( allowEmpty = true )
abstract class ThrowsModel
{
  @LocatorRef
  protected abstract Locator getLocator()
    throws Exception;

  @Reference
  abstract MyEntity getMyEntity();

  @ReferenceId
  int getMyEntityId()
  {
    return 0;
  }

  static class MyEntity
  {
  }
}
