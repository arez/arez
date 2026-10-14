package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import java.util.Collection;
import java.util.Collections;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class MemoizeCollectionWithHooksModel
{
  @Memoize
  public @Nullable Collection<Long> getTime()
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
