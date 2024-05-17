package com.example.inheritance.interface_inheritance;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
abstract class MyModel
  implements MyInterface
{
  MyModel( String valueA )
  {
  }

  @Observable
  abstract String getValueB();

  abstract void setValueB( String valueB );

  @Observable
  public abstract String getValueC();

  abstract void setValueC( String valueC );
}
