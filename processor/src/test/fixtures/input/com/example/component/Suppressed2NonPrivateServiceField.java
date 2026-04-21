package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.SuppressArezWarnings;

@ArezComponent( allowEmpty = true )
abstract class Suppressed2NonPrivateServiceField
{
  @SuppressArezWarnings( "Arez:NonPrivateServiceField" )
  final MyService _myService = null;

  @ArezComponent( allowEmpty = true, service = Feature.ENABLE )
  public abstract static class MyService
  {
  }
}
