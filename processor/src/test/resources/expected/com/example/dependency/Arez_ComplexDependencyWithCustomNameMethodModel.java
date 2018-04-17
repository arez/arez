package com.example.dependency;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputedValue;
import arez.Disposable;
import arez.EqualityComparator;
import arez.Observable;
import arez.Observer;
import arez.SafeFunction;
import arez.component.ComponentObservable;
import arez.component.ComponentState;
import arez.component.Identifiable;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
public final class Arez_ComplexDependencyWithCustomNameMethodModel extends ComplexDependencyWithCustomNameMethodModel implements Disposable, Identifiable<Long>, ComponentObservable {
  private static volatile long $$arezi$$_nextId;

  private final long $$arezi$$_id;

  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final Observable<Boolean> $$arezi$$_disposedObservable;

  @Nonnull
  private final Observable<Object> $$arez$$_value3;

  @Nonnull
  private final ComputedValue<Object> $$arez$$_value2;

  @Nonnull
  private final Observer $$arezi$$_cascadeOnDispose;

  @Nonnull
  private final Observer $$arezi$$_setNullOnDispose;

  public Arez_ComplexDependencyWithCustomNameMethodModel() {
    super();
    this.$$arezi$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arezi$$_id = ( Arez.areNativeComponentsEnabled() || Arez.areNativeComponentsEnabled() ) ? $$arezi$$_nextId++ : 0L;
    this.$$arezi$$_state = ComponentState.COMPONENT_INITIALIZED;
    this.$$arezi$$_component = Arez.areNativeComponentsEnabled() ? $$arezi$$_context().createComponent( "ComplexDependencyWithCustomNameMethodModel", $$arezi$$_id(), getComponentName(), null, null ) : null;
    this.$$arezi$$_disposedObservable = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getComponentName() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezi$$_state >= 0 : null, null );
    this.$$arez$$_value3 = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getComponentName() + ".value3" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getValue3() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setValue3( v ) : null );
    this.$$arez$$_value2 = $$arezi$$_context().createComputedValue( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getComponentName() + ".value2" : null, () -> super.getValue2(), EqualityComparator.defaultComparator(), null, null, null, null );
    this.$$arezi$$_cascadeOnDispose = $$arezi$$_context().when( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getComponentName() + ".cascadeOnDispose" : null, false, () -> $$arezi$$_getCascadeOnDisposeDependencies().map( SafeFunction::call ).peek( ComponentObservable::observe ).anyMatch( Disposable::isDisposed ), () -> Disposable.dispose( this ), true, false );
    this.$$arezi$$_setNullOnDispose = $$arezi$$_context().autorun( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getComponentName() + ".setNullOnDispose" : null, true, () -> $$arezi$$_setNullOnDispose(), true, false );
    this.$$arezi$$_state = ComponentState.COMPONENT_CONSTRUCTED;
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arezi$$_component.complete();
    }
    this.$$arezi$$_state = ComponentState.COMPONENT_COMPLETE;
    $$arezi$$_context().triggerScheduler();
    this.$$arezi$$_state = ComponentState.COMPONENT_READY;
  }

  final ArezContext $$arezi$$_context() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_context' invoked on uninitialized component of type 'ComplexDependencyWithCustomNameMethodModel'" );
    }
    return Arez.areZonesEnabled() ? this.$$arezi$$_context : Arez.context();
  }

  final long $$arezi$$_id() {
    if ( Arez.shouldCheckInvariants() && !Arez.areNamesEnabled() && !Arez.areNativeComponentsEnabled() ) {
      Guards.fail( () -> "Method invoked to access id when id not expected." );
    }
    return this.$$arezi$$_id;
  }

  @Override
  @Nonnull
  public final Long getArezId() {
    return $$arezi$$_id();
  }

  @Nonnull
  final String getComponentName() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named 'getComponentName' invoked on uninitialized component of type 'ComplexDependencyWithCustomNameMethodModel'" );
    }
    return "ComplexDependencyWithCustomNameMethodModel." + $$arezi$$_id();
  }

  private final Stream<SafeFunction<Object>> $$arezi$$_getCascadeOnDisposeDependencies() {
    return Stream.of(() -> getValue1(), () -> getValue2());
  }

  private final void $$arezi$$_setNullOnDispose() {
    final Object dependency1 = getValue3();
    if ( !ComponentObservable.observe( dependency1 ) )  {
      setValue3( null );
    } ;
  }

  private boolean $$arezi$$_observe() {
    final boolean isDisposed = isDisposed();
    if ( !isDisposed )  {
      this.$$arezi$$_disposedObservable.reportObserved();
    }
    return !isDisposed;
  }

  @Override
  public boolean observe() {
    return $$arezi$$_observe();
  }

  @Override
  public boolean isDisposed() {
    return ComponentState.isDisposingOrDisposed( this.$$arezi$$_state );
  }

  @Override
  public void dispose() {
    if ( !ComponentState.isDisposingOrDisposed( this.$$arezi$$_state ) ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSING;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arezi$$_component.dispose();
      } else {
        $$arezi$$_context().dispose( Arez.areNamesEnabled() ? getComponentName() : null, () -> { {
          this.$$arezi$$_disposedObservable.dispose();
          this.$$arezi$$_cascadeOnDispose.dispose();
          this.$$arezi$$_setNullOnDispose.dispose();
          this.$$arez$$_value2.dispose();
          this.$$arez$$_value3.dispose();
        } } );
      }
      this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSED;
    }
  }

  @Override
  Object getValue3() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getValue3' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    this.$$arez$$_value3.reportObserved();
    return super.getValue3();
  }

  @Override
  void setValue3(final Object value) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'setValue3' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    if ( !Objects.equals( value, super.getValue3() ) ) {
      this.$$arez$$_value3.preReportChanged();
      super.setValue3(value);
      this.$$arez$$_value3.reportChanged();
    }
  }

  @Override
  public Object getValue2() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getValue2' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    return this.$$arez$$_value2.get();
  }

  @Override
  public final int hashCode() {
    if ( Arez.areNativeComponentsEnabled() ) {
      return Long.hashCode( $$arezi$$_id() );
    } else {
      return super.hashCode();
    }
  }

  @Override
  public final boolean equals(final Object o) {
    if ( Arez.areNativeComponentsEnabled() ) {
      if ( this == o ) {
        return true;
      } else if ( null == o || !(o instanceof Arez_ComplexDependencyWithCustomNameMethodModel) ) {
        return false;
      } else {
        final Arez_ComplexDependencyWithCustomNameMethodModel that = (Arez_ComplexDependencyWithCustomNameMethodModel) o;;
        return $$arezi$$_id() == that.$$arezi$$_id();
      }
    } else {
      return super.equals( o );
    }
  }

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + getComponentName() + "]";
    } else {
      return super.toString();
    }
  }
}
