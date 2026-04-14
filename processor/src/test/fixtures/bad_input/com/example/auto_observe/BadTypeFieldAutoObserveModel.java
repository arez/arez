package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;

@ArezComponent( allowEmpty = true )
abstract class BadTypeFieldAutoObserveModel
{
  @AutoObserve
  final Object _field = null;
}
