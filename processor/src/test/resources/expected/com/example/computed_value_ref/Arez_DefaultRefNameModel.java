package com.example.computed_value_ref;

import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Component;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.braincheck.Guards;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
public final class Arez_DefaultRefNameModel extends DefaultRefNameModel implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  private final Component $$arez$$_component;

  private final Observable<Boolean> $$arez$$_disposedObservable;

  @Nonnull
  private final ComputedValue<Long> $$arez$$_time;

  public Arez_DefaultRefNameModel() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_component = Arez.areNativeComponentsEnabled() ? this.$$arez$$_context.createComponent( "DefaultRefNameModel", $$arez$$_id(), $$arez$$_name(), null, null ) : null;
    this.$$arez$$_disposedObservable = this.$$arez$$_context.createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arez$$_disposed : null, null );
    this.$$arez$$_time = this.$$arez$$_context.createComputedValue( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".time" : null, super::getTime, Objects::equals, null, null, null, null );
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arez$$_component.complete();
    }
  }

  final long $$arez$$_id() {
    return this.$$arez$$_id;
  }

  String $$arez$$_name() {
    return "DefaultRefNameModel." + $$arez$$_id();
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
        this.$$arez$$_context.safeAction( Arez.areNamesEnabled() ? $$arez$$_name() + ".dispose" : null, () -> { {
          this.$$arez$$_disposedObservable.dispose();
          this.$$arez$$_time.dispose();
        } } );
      }
    }
  }

  @Override
  public long getTime() {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    return this.$$arez$$_time.get();
  }

  @Nonnull
  @Override
  ComputedValue<Long> getTimeComputedValue() {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    return $$arez$$_time;
  }

  @Override
  public final int hashCode() {
    return Long.hashCode( $$arez$$_id() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_DefaultRefNameModel) ) {
      return false;
    } else {
      final Arez_DefaultRefNameModel that = (Arez_DefaultRefNameModel) o;;
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
