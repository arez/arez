package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.SuppressArezWarnings;

// This uses the SOURCE retention suppression
@SuppressArezWarnings( "Arez:UnnecessaryAllowEmpty" )
@ArezComponent( allowEmpty = true )
public abstract class Suppressed2UnnecessaryAllowEmptyPresentComponent
{
  @Action
  void myAction()
  {
  }
}
