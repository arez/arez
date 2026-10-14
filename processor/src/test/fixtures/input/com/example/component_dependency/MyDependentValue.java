package com.example.component_dependency;

import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MyDependentValue
  implements DisposeNotifier
{
  @Override
  public void addOnDisposeListener( @NonNull final Object key,
                                    @NonNull final SafeProcedure action,
                                    final boolean errorIfDuplicate )
  {
  }

  @Override
  public void removeOnDisposeListener( @NonNull final Object key, final boolean errorIfMissing )
  {
  }
}
