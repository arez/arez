package com.example.component;

import arez.annotations.ArezComponent;

@ArezComponent( allowEmpty = true )
public abstract class UnresolvedComponent
{
  static Arez_UnresolvedComponent create()
  {
    return new Arez_UnresolvedComponent();
  }
}
