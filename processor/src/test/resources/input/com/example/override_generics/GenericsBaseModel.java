package com.example.override_generics;

import arez.annotations.Action;

public class GenericsBaseModel<V>
{
  @Action
  public void foo( final V v )
  {
  }
}
