package com.example.inject;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Observable;
import arez.component.Identifiable;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public final class Arez_DefaultCtorModel extends DefaultCtorModel implements Disposable, Identifiable<Long> {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private byte $$arez$$_state;

  @Nullable
  private final ArezContext $$arez$$_context;

  private final Component $$arez$$_component;

  private final Observable<Boolean> $$arez$$_disposedObservable;

  @Inject
  public Arez_DefaultCtorModel() {
    super();
    this.$$arez$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_state = 1;
    this.$$arez$$_component = Arez.areNativeComponentsEnabled() ? $$arez$$_context().createComponent( "DefaultCtorModel", $$arez$$_id(), $$arez$$_name(), null, null ) : null;
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
    return "DefaultCtorModel." + $$arez$$_id();
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
  public void myActionStuff() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
    }
    try {
      $$arez$$_context().safeAction(Arez.areNamesEnabled() ? $$arez$$_name() + ".myActionStuff" : null, true, () -> super.myActionStuff() );
    } catch( final RuntimeException | Error $$arez$$_e ) {
      throw $$arez$$_e;
    } catch( final Throwable $$arez$$_e ) {
      throw new IllegalStateException( $$arez$$_e );
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
    } else if ( null == o || !(o instanceof Arez_DefaultCtorModel) ) {
      return false;
    } else {
      final Arez_DefaultCtorModel that = (Arez_DefaultCtorModel) o;;
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
