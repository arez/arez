package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

// This uses the SOURCE retention suppression
@SuppressWarnings( "Arez:UnnecessaryAllowEmpty" )
@ArezComponent( allowEmpty = true )
public abstract class Suppressed1UnnecessaryAllowEmptyPresentComponent
{
  @Action
  void myAction()
  {
  }
}
