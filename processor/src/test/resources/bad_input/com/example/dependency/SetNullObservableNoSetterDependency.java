package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import arez.component.DisposeTrackable;

@ArezComponent( allowEmpty = true )
public abstract class SetNullObservableNoSetterDependency
{
  @Observable(expectSetter = false)
  @Dependency( action = Dependency.Action.SET_NULL )
  DisposeTrackable getTime()
  {
    return null;
  }

  @ObservableRef
  abstract arez.Observable<DisposeTrackable> getTimeObservable();
}
