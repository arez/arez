package com.example.computed_value_ref;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputedValue;
import arez.Disposable;
import arez.Observable;
import arez.component.ComponentState;
import arez.component.Identifiable;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
public final class Arez_DefaultRefNameModel extends DefaultRefNameModel implements Disposable, Identifiable<Long> {
  private static volatile long $$arezi$$_nextId;

  private final long $$arezi$$_id;

  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final Observable<Boolean> $$arezi$$_disposedObservable;

  @Nonnull
  private final ComputedValue<Long> $$arez$$_time;

  public Arez_DefaultRefNameModel() {
    super();
    this.$$arezi$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arezi$$_id = $$arezi$$_nextId++;
    this.$$arezi$$_state = ComponentState.COMPONENT_INITIALIZED;
    this.$$arezi$$_component = Arez.areNativeComponentsEnabled() ? $$arezi$$_context().createComponent( "DefaultRefNameModel", $$arezi$$_id(), $$arezi$$_name(), null, null ) : null;
    this.$$arezi$$_disposedObservable = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezi$$_state >= 0 : null, null );
    this.$$arez$$_time = $$arezi$$_context().createComputedValue( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".time" : null, super::getTime, Objects::equals, null, null, null, null );
    this.$$arezi$$_state = ComponentState.COMPONENT_CONSTRUCTED;
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arezi$$_component.complete();
    }
    this.$$arezi$$_state = ComponentState.COMPONENT_READY;
  }

  final ArezContext $$arezi$$_context() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method invoked on uninitialized component of type 'DefaultRefNameModel'" );
    }
    return Arez.areZonesEnabled() ? this.$$arezi$$_context : Arez.context();
  }

  final long $$arezi$$_id() {
    return this.$$arezi$$_id;
  }

  @Override
  @Nonnull
  public final Long getArezId() {
    return $$arezi$$_id();
  }

  String $$arezi$$_name() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method invoked on uninitialized component of type 'DefaultRefNameModel'" );
    }
    return "DefaultRefNameModel." + $$arezi$$_id();
  }

  @Override
  public boolean isDisposed() {
    if ( $$arezi$$_context().isTransactionActive() && !this.$$arezi$$_disposedObservable.isDisposed() )  {
      this.$$arezi$$_disposedObservable.reportObserved();
    }
    return ComponentState.isDisposingOrDisposed( this.$$arezi$$_state );
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSING;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arezi$$_component.dispose();
      } else {
        $$arezi$$_context().dispose( Arez.areNamesEnabled() ? $$arezi$$_name() : null, () -> { {
          this.$$arezi$$_disposedObservable.dispose();
          this.$$arez$$_time.dispose();
        } } );
      }
      this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSED;
    }
  }

  @Override
  public long getTime() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    return this.$$arez$$_time.get();
  }

  @Nonnull
  @Override
  ComputedValue<Long> getTimeComputedValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    return $$arez$$_time;
  }

  @Override
  public final int hashCode() {
    return Long.hashCode( $$arezi$$_id() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_DefaultRefNameModel) ) {
      return false;
    } else {
      final Arez_DefaultRefNameModel that = (Arez_DefaultRefNameModel) o;;
      return $$arezi$$_id() == that.$$arezi$$_id();
    }
  }

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + $$arezi$$_name() + "]";
    } else {
      return super.toString();
    }
  }
}
