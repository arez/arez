package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class Suppressed1PublicAccessFieldDependencyModel
{
  @SuppressWarnings( "Arez:PublicField" )
  @ComponentDependency
  public final @Nullable DisposeNotifier time = null;
}
