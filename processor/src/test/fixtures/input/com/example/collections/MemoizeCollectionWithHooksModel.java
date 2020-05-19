package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
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
  void onTimeActivate()
  {
  }

  @OnDeactivate
  void onTimeDeactivate()
  {
  }
}
