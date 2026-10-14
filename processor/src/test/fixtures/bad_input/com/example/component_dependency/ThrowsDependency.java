package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;
import java.io.IOException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class ThrowsDependency
{
  @SuppressWarnings( { "RedundantThrows", "RedundantSuppression" } )
  @ComponentDependency
  final @Nullable DisposeNotifier getTime()
    throws IOException
  {
    return null;
  }
}
