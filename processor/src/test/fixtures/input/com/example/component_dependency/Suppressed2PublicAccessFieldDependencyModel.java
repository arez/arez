package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.SuppressArezWarnings;
import arez.component.DisposeNotifier;

@ArezComponent
abstract class Suppressed2PublicAccessFieldDependencyModel
{
  @SuppressArezWarnings( "Arez:PublicField" )
  @ComponentDependency
  public final DisposeNotifier time = null;
}
