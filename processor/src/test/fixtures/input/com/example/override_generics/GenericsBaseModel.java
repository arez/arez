package com.example.override_generics;

import arez.annotations.Action;

abstract class GenericsBaseModel<V>
{
  @Action
  public void foo( final V v )
  {
  }
}
