package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;

@ArezComponent
abstract class RuntimeValidateWithoutActAsComponentFieldAutoObserveModel
{
  @AutoObserve( validateTypeAtRuntime = true )
  final Object _field = null;
}
