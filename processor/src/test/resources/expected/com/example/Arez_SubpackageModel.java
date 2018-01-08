package com.example;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputedValue;
import arez.Disposable;
import arez.Observable;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
public final class Arez_SubpackageModel extends SubpackageModel implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  @Nullable
  private final ArezContext $$arez$$_context;

  private final Component $$arez$$_component;

  private final Observable<Boolean> $$arez$$_disposedObservable;

  @Nonnull
  private final Observable<Long> $$arez$$_time;

  @Nonnull
  private final ComputedValue<Integer> $$arez$$_someValue;

  public Arez_SubpackageModel() {
    super();
    this.$$arez$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_component = Arez.areNativeComponentsEnabled() ? $$arez$$_context().createComponent( "SubpackageModel", $$arez$$_id(), $$arez$$_name(), null, null ) : null;
    this.$$arez$$_disposedObservable = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arez$$_disposed : null, null );
    this.$$arez$$_time = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".time" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getTime() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setTime( v ) : null );
    this.$$arez$$_someValue = $$arez$$_context().createComputedValue( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".someValue" : null, super::someValue, Objects::equals, null, null, null, null );
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arez$$_component.complete();
    }
  }

  final ArezContext $$arez$$_context() {
    return Arez.areZonesEnabled() ? this.$$arez$$_context : Arez.context();
  }

  final long $$arez$$_id() {
    return this.$$arez$$_id;
  }

  String $$arez$$_name() {
    return "SubpackageModel." + $$arez$$_id();
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
          this.$$arez$$_someValue.dispose();
          this.$$arez$$_time.dispose();
        } } );
      }
    }
  }

  @Override
  public long getTime() {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    this.$$arez$$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime(final long time) {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    if ( time != super.getTime() ) {
      super.setTime(time);
      this.$$arez$$_time.reportChanged();
    }
  }

  @Override
  public void doStuff(final long time) {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    try {
      $$arez$$_context().safeAction(Arez.areNamesEnabled() ? $$arez$$_name() + ".doStuff" : null, true, () -> super.doStuff(time), time );
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
  public int someValue() {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    return this.$$arez$$_someValue.get();
  }

  @Override
  public final int hashCode() {
    return Long.hashCode( $$arez$$_id() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_SubpackageModel) ) {
      return false;
    } else {
      final Arez_SubpackageModel that = (Arez_SubpackageModel) o;;
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
