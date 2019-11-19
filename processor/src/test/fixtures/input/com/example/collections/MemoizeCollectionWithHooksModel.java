package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.OnStale;
import java.util.Collection;
import java.util.Collections;

@ArezComponent
abstract class MemoizeCollectionWithHooksModel
{
  @Memoize
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
}
