package com.example.component_ref;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Observable;
import arez.component.Identifiable;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
final class Arez_ProtectedAccessComponent extends ProtectedAccessComponent implements Disposable, Identifiable<Long> {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private byte $$arez$$_state;

  @Nullable
  private final ArezContext $$arez$$_context;

  private final Component $$arez$$_component;

  private final Observable<Boolean> $$arez$$_disposedObservable;

  Arez_ProtectedAccessComponent() {
    super();
    this.$$arez$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_state = 1;
    this.$$arez$$_component = Arez.areNativeComponentsEnabled() ? $$arez$$_context().createComponent( "ProtectedAccessComponent", $$arez$$_id(), $$arez$$_name(), null, null ) : null;
    this.$$arez$$_disposedObservable = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arez$$_state >= 0 : null, null );
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arez$$_component.complete();
    }
    this.$$arez$$_state = 2;
    this.$$arez$$_state = 3;
  }

  final ArezContext $$arez$$_context() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state == 0, () -> "Method invoked on uninitialized component named '" + $$arez$$_name() + "'" );
    }
    return Arez.areZonesEnabled() ? this.$$arez$$_context : Arez.context();
  }

  protected final Component getComponent() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state == 0 || this.$$arez$$_state == 1, () -> "Method invoked on un-constructed component named '" + $$arez$$_name() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
    }
    if ( Arez.shouldCheckInvariants() ) {
      Guards.invariant( () -> Arez.areNativeComponentsEnabled(), () -> "Invoked @ComponentRef method 'getComponent' but Arez.areNativeComponentsEnabled() returned false." );
    }
    return this.$$arez$$_component;
  }

  final long $$arez$$_id() {
    return this.$$arez$$_id;
  }

  @Override
  @Nonnull
  public final Long getArezId() {
    return $$arez$$_id();
  }

  String $$arez$$_name() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state == 0, () -> "Method invoked on uninitialized component named '" + $$arez$$_name() + "'" );
    }
    return "ProtectedAccessComponent." + $$arez$$_id();
  }

  @Override
  public boolean isDisposed() {
    if ( $$arez$$_context().isTransactionActive() && !this.$$arez$$_disposedObservable.isDisposed() )  {
      this.$$arez$$_disposedObservable.reportObserved();
    }
    return this.$$arez$$_state < 0;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      this.$$arez$$_state = -2;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arez$$_component.dispose();
      } else {
        $$arez$$_context().safeAction( Arez.areNamesEnabled() ? $$arez$$_name() + ".dispose" : null, () -> { {
          this.$$arez$$_disposedObservable.dispose();
        } } );
      }
      this.$$arez$$_state = -1;
    }
  }

  @Override
  public final int hashCode() {
    return Long.hashCode( $$arez$$_id() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_ProtectedAccessComponent) ) {
      return false;
    } else {
      final Arez_ProtectedAccessComponent that = (Arez_ProtectedAccessComponent) o;;
      return $$arez$$_id() == that.$$arez$$_id();
    }
  }

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + $$arez$$_name() + "]";
    } else {
      return super.toString();
    }
  }
}
