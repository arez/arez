package com.example.entity_locator;

import arez.Locator;
import arez.annotations.ArezComponent;
import arez.annotations.LocatorRef;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nonnull;

@ArezComponent( allowEmpty = true )
abstract class ConstructorNameCollisionLocatorModel
{
  ConstructorNameCollisionLocatorModel( @Nonnull final Locator locator )
  {
  }

  @LocatorRef
  protected abstract Locator getLocator();

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
