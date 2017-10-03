package com.example.override_generics;

import org.realityforge.arez.annotations.Action;

public class GenericsMiddleModel<V extends Number>
  extends GenericsBaseModel<V>
{
  @Action
  @Override
  public void foo( final V v )
  {
    super.foo( v );
  }
}
