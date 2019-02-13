package com.example.dispose_notifier;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Repository;

@Repository
@ArezComponent( disposeNotifier = Feature.DISABLE )
public abstract class NoDisposeNotifierWithRepositoryModel
{
  @Action
  public void someValue()
  {
  }
}
