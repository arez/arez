package com.example.component_name;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Component;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.braincheck.Guards;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_ComponentNameOnSingletonModel extends ComponentNameOnSingletonModel implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  private final Component $$arez$$_component;

  private final Observable<Boolean> $$arez$$_disposedObservable;

  public Arez_ComponentNameOnSingletonModel() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_component = Arez.areNativeComponentsEnabled() ? this.$$arez$$_context.createComponent( "ComponentNameOnSingletonModel", $$arez$$_id(), getTypeName(), null, null ) : null;
    this.$$arez$$_disposedObservable = this.$$arez$$_context.createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? getTypeName() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arez$$_disposed : null, null );
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arez$$_component.complete();
    }
  }

  final long $$arez$$_id() {
    return this.$$arez$$_id;
  }

  final String getTypeName() {
    return "ComponentNameOnSingletonModel";
  }

  @Override
  public boolean isDisposed() {
    if ( this.$$arez$$_context.isTransactionActive() && !this.$$arez$$_disposedObservable.isDisposed() )  {
      this.$$arez$$_disposedObservable.reportObserved();
      return this.$$arez$$_disposed;
    } else {
      return this.$$arez$$_disposed;
    }
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      this.$$arez$$_disposed = true;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arez$$_component.dispose();
      } else {
        this.$$arez$$_context.safeAction( Arez.areNamesEnabled() ? getTypeName() + ".dispose" : null, () -> { {
          this.$$arez$$_disposedObservable.dispose();
        } } );
      }
    }
  }

  @Override
  void myAction() {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + getTypeName() + "'" );
    try {
      this.$$arez$$_context.safeAction(Arez.areNamesEnabled() ? getTypeName() + ".myAction" : null, true, () -> super.myAction() );
    } catch( final RuntimeException $$arez$$_e ) {
      throw $$arez$$_e;
    } catch( final Exception $$arez$$_e ) {
      throw new IllegalStateException( $$arez$$_e );
    } catch( final Error $$arez$$_e ) {
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
    } else if ( null == o || !(o instanceof Arez_ComponentNameOnSingletonModel) ) {
      return false;
    } else {
      final Arez_ComponentNameOnSingletonModel that = (Arez_ComponentNameOnSingletonModel) o;;
      return $$arez$$_id() == that.$$arez$$_id();
    }
  }

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + getTypeName() + "]";
    } else {
      return super.toString();
    }
  }
}
