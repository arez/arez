package com.example.component_ref;

import org.realityforge.arez.Component;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
class DuplicateModel
{
  @ComponentRef
  Component getComponent()
  {
    throw new IllegalStateException();
  }

  @ComponentRef
  Component getComponent2()
  {
    throw new IllegalStateException();
  }
}
