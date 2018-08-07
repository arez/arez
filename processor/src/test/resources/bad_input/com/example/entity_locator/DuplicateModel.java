package com.example.entity_locator;

import arez.Locator;
import arez.annotations.ArezComponent;
import arez.annotations.LocatorRef;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent( allowEmpty = true )
abstract class DuplicateModel
{
  @LocatorRef
  protected abstract Locator getLocator();

  @LocatorRef
  protected abstract Locator getLocator2();

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
