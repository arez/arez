package com.example.override_generics;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

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
