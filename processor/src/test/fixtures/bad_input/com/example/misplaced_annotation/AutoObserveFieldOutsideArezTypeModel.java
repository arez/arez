package com.example.misplaced_annotation;

import arez.annotations.AutoObserve;

abstract class AutoObserveFieldOutsideArezTypeModel
{
  @AutoObserve
  final Object dependency = null;
}
