package com.example.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Feature;

@ArezComponent( disposeNotifier = Feature.DISABLE )
abstract class DisposeNotifierDisabledCascadeDisposeModel
{
  @CascadeDispose
  final Disposable _myObject = null;
}
