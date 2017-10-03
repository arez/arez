package com.example.override_generics;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

@ArezComponent
public class GenericsModel
  extends GenericsMiddleModel<Integer>
{
  @Action
  @Override
  public void foo( final Integer v )
  {
    super.foo( v );
  }
}
