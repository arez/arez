package com.example.entity_locator;

import arez.annotations.ArezComponent;
import arez.annotations.LocatorRef;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.component.Locator;

@ArezComponent( allowEmpty = true )
abstract class StaticModel
{
  @LocatorRef
  static Locator getLocator()
  {
    return null;
  }

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
