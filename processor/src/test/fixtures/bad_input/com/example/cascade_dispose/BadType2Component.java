package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;

@ArezComponent
public abstract class BadType2Component
{
  // Class does not implement Disposable
  @CascadeDispose
  String _myField;
}
