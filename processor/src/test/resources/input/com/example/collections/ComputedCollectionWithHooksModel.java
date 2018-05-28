package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.OnDispose;
import arez.annotations.OnStale;
import java.util.Collection;
import java.util.Collections;

@ArezComponent
public abstract class ComputedCollectionWithHooksModel
{
  @Computed
  public Collection<Long> getTime()
  {
    return Collections.emptyList();
  }

  @OnActivate
  final void onTimeActivate()
  {
  }

  @OnDeactivate
  final void onTimeDeactivate()
  {
  }

  @OnStale
  final void onTimeStale()
  {
  }

  @OnDispose
  final void onTimeDispose()
  {
  }
}
