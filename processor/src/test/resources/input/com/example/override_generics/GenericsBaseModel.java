package com.example.override_generics;

import org.realityforge.arez.annotations.Action;

public class GenericsBaseModel<V>
{
  @Action
  public void foo( final V v )
  {
  }
}
