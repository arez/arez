package com.example.misplaced_annotation;

import arez.annotations.ActAsComponent;
import arez.annotations.Action;
import arez.annotations.AutoObserve;
import arez.annotations.SuppressArezWarnings;

@ActAsComponent
@SuppressArezWarnings( "Arez:PublicField" )
abstract class ActAsComponentUsageModel
{
  @Action
  void perform()
  {
  }

  @AutoObserve
  final Object dependency = null;
}
