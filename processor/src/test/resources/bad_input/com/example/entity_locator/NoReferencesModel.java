package com.example.entity_locator;

import arez.annotations.ArezComponent;
import arez.annotations.LocatorRef;
import arez.component.Locator;

@ArezComponent( allowEmpty = true )
abstract class NoReferencesModel
{
  @LocatorRef
  protected abstract Locator getLocator();
}
