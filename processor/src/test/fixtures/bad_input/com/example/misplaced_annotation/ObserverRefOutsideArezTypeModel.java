package com.example.misplaced_annotation;

import arez.annotations.ObserverRef;

final class ObserverRefOutsideArezTypeModel
{
  @ObserverRef
  Object getObserver()
  {
    return null;
  }
}
