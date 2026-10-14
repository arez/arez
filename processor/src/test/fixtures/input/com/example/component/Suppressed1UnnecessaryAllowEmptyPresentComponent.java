package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

// This uses the SOURCE retention suppression
@NullMarked
@SuppressWarnings( "Arez:UnnecessaryAllowEmpty" )
@ArezComponent( allowEmpty = true )
public abstract class Suppressed1UnnecessaryAllowEmptyPresentComponent
{
  @Action
  void myAction()
  {
  }
}
