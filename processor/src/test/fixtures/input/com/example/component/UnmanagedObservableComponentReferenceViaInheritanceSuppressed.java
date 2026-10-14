package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
abstract class UnmanagedObservableComponentReferenceViaInheritanceSuppressed
{
  @NullMarked
  @ArezComponent
  public abstract static class MyComponent
    extends Base
  {
  }

  @NullMarked
  abstract static class Base
  {
    @SuppressWarnings( "Arez:UnmanagedComponentReference" )
    @Observable
    abstract @Nullable OtherComponent getMyComponent();

    abstract void setMyComponent( @Nullable OtherComponent component );
  }

  @NullMarked
  @ArezComponent( allowEmpty = true )
  abstract static class OtherComponent
  {
  }
}
