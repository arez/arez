package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;

// This uses the SOURCE retention suppression
@NullMarked
@SuppressArezWarnings( "Arez:UnnecessaryAllowEmpty" )
@ArezComponent( allowEmpty = true )
public abstract class Suppressed2UnnecessaryAllowEmptyPresentComponent
{
  @Action
  void myAction()
  {
  }
}
