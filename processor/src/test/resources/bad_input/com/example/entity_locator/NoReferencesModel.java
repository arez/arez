package com.example.entity_locator;

import arez.Locator;
import arez.annotations.ArezComponent;
import arez.annotations.LocatorRef;

@ArezComponent( allowEmpty = true )
abstract class NoReferencesModel
{
  @LocatorRef
  protected abstract Locator getLocator();
}
