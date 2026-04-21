package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( allowEmpty = true )
abstract class Suppressed1NonPrivateServiceField
{
  @SuppressWarnings( "Arez:NonPrivateServiceField" )
  final MyService _myService = null;

  @ArezComponent( allowEmpty = true, service = Feature.ENABLE )
  public abstract static class MyService
  {
  }
}
