package com.example.inheritance.interface_inheritance;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class MyModel
  implements MyInterface
{
  MyModel( @Nullable String valueA )
  {
  }

  @Observable
  abstract @Nullable String getValueB();

  abstract void setValueB( @Nullable String valueB );

  @Observable
  public abstract @Nullable String getValueC();

  abstract void setValueC( @Nullable String valueC );
}
