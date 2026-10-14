package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class NonnullAbstractObservableDependency
{
  @Observable
  @ComponentDependency
  @NonNull
  abstract DisposeNotifier getValue();

  abstract void setValue( @NonNull DisposeNotifier value );
}
