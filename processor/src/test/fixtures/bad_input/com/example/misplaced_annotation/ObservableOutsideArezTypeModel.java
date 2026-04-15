package com.example.misplaced_annotation;

import arez.annotations.Observable;

final class ObservableOutsideArezTypeModel
{
  @Observable
  String getName()
  {
    return "";
  }
}
