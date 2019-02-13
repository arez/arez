package com.example.component_dependency;

import arez.SafeProcedure;
import arez.component.DisposeTrackable;
import javax.annotation.Nonnull;

public class MyDependentValue
  implements DisposeTrackable
{
  @Override
  public void addOnDisposeListener( @Nonnull final Object key, @Nonnull final SafeProcedure action )
  {
  }

  @Override
  public void removeOnDisposeListener( @Nonnull final Object key )
  {
  }
}
