package com.example.component_dependency;

import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import javax.annotation.Nonnull;

public class MyDependentValue
  implements DisposeNotifier
{
  @Override
  public void addOnDisposeListener( @Nonnull final Object key,
                                    @Nonnull final SafeProcedure action,
                                    final boolean errorIfDuplicate )
  {
  }

  @Override
  public void removeOnDisposeListener( @Nonnull final Object key, final boolean errorIfMissing )
  {
  }
}
