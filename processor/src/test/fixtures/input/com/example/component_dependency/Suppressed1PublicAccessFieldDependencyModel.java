package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;

@ArezComponent
abstract class Suppressed1PublicAccessFieldDependencyModel
{
  @SuppressWarnings( "Arez:PublicField" )
  @ComponentDependency
  public final DisposeNotifier time = null;
}
