package com.example.memoize;

import arez.ComputableValue;
import arez.annotations.*;
import java.io.IOException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent(requireEquals = Feature.ENABLE)
@SuppressWarnings({"Arez:PublicHookMethod", "Arez:PublicLifecycleMethod"})
interface InterfaceHooksModel<T extends @Nullable Object>
{
  @RequiresTransaction
  default int combine(int left, int right) throws IOException
  {
    return left + right;
  }

  @Memoize(depType = DepType.AREZ_OR_EXTERNAL, readOutsideTransaction = Feature.ENABLE)
  default int getValue()
  {
    return 1;
  }

  @OnActivate
  default void onValueActivate(ComputableValue<Integer> value)
  {
  }

  @OnDeactivate
  default void onValueDeactivate()
  {
  }

  @Observe(executor = Executor.EXTERNAL)
  default void refresh()
  {
    getValue();
  }

  @OnDepsChange
  default void onRefreshDepsChange()
  {
  }

  @PreDispose
  default void beforeDispose()
  {
  }

  @PostDispose
  default void afterDispose()
  {
  }

  @PostDispose
  default void afterDisposeAgain()
  {
  }
}
