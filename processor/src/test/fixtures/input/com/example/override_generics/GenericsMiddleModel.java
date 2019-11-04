package com.example.override_generics;

import arez.annotations.Action;

public abstract class GenericsMiddleModel<V extends Number>
  extends GenericsBaseModel<V>
{
  @Action
  @Override
  public void foo( final V v )
  {
    super.foo( v );
  }
}
