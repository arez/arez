package com.example.component_id;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Observable;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("arez.processor.ArezProcessor")
public final class Arez_DoubleComponentId extends DoubleComponentId implements Disposable {
  private boolean $$arez$$_disposed;

  @Nullable
  private final ArezContext $$arez$$_context;

  private final Component $$arez$$_component;

  private final Observable<Boolean> $$arez$$_disposedObservable;

  public Arez_DoubleComponentId() {
    super();
    this.$$arez$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arez$$_component = Arez.areNativeComponentsEnabled() ? $$arez$$_context().createComponent( "DoubleComponentId", getId(), $$arez$$_name(), null, null ) : null;
    this.$$arez$$_disposedObservable = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arez$$_disposed : null, null );
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arez$$_component.complete();
    }
  }

  final ArezContext $$arez$$_context() {
    return Arez.areZonesEnabled() ? this.$$arez$$_context : Arez.context();
  }

  String $$arez$$_name() {
    return "DoubleComponentId." + getId();
  }

  @Override
  public boolean isDisposed() {
    if ( $$arez$$_context().isTransactionActive() && !this.$$arez$$_disposedObservable.isDisposed() )  {
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
        $$arez$$_context().safeAction( Arez.areNamesEnabled() ? $$arez$$_name() + ".dispose" : null, () -> { {
          this.$$arez$$_disposedObservable.dispose();
        } } );
      }
    }
  }

  @Override
  public final int hashCode() {
    return Double.hashCode( getId() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_DoubleComponentId) ) {
      return false;
    } else {
      final Arez_DoubleComponentId that = (Arez_DoubleComponentId) o;;
      return getId() == that.getId();
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
