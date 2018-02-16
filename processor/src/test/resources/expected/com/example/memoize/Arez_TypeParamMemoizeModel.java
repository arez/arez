package com.example.memoize;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Observable;
import arez.component.Identifiable;
import arez.component.MemoizeCache;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public final class Arez_TypeParamMemoizeModel<T> extends TypeParamMemoizeModel<T> implements Disposable, Identifiable<Long> {
  private static volatile long $$arezi$$_nextId;

  private final long $$arezi$$_id;

  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final Observable<Boolean> $$arezi$$_disposedObservable;

  @Nonnull
  private final MemoizeCache<T> $$arez$$_count;

  public Arez_TypeParamMemoizeModel() {
    super();
    this.$$arezi$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arezi$$_id = $$arezi$$_nextId++;
    this.$$arezi$$_state = 1;
    this.$$arezi$$_component = Arez.areNativeComponentsEnabled() ? $$arezi$$_context().createComponent( "TypeParamMemoizeModel", $$arezi$$_id(), $$arezi$$_name(), null, null ) : null;
    this.$$arezi$$_disposedObservable = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezi$$_state >= 0 : null, null );
    this.$$arez$$_count = new MemoizeCache<>( $$arezi$$_context(), Arez.areNamesEnabled() ? $$arezi$$_name() + ".count" : null, args -> super.count((String) args[ 0 ]), 1);
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arezi$$_component.complete();
    }
    this.$$arezi$$_state = 2;
    this.$$arezi$$_state = 3;
  }

  final ArezContext $$arezi$$_context() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arezi$$_state == 0, () -> "Method invoked on uninitialized component named '" + $$arezi$$_name() + "'" );
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
      Guards.apiInvariant( () -> this.$$arezi$$_state == 0, () -> "Method invoked on uninitialized component named '" + $$arezi$$_name() + "'" );
    }
    return "TypeParamMemoizeModel." + $$arezi$$_id();
  }

  @Override
  public boolean isDisposed() {
    if ( $$arezi$$_context().isTransactionActive() && !this.$$arezi$$_disposedObservable.isDisposed() )  {
      this.$$arezi$$_disposedObservable.reportObserved();
    }
    return this.$$arezi$$_state < 0;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      this.$$arezi$$_state = -2;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arezi$$_component.dispose();
      } else {
        $$arezi$$_context().safeAction( Arez.areNamesEnabled() ? $$arezi$$_name() + ".dispose" : null, () -> { {
          this.$$arezi$$_disposedObservable.dispose();
          this.$$arez$$_count.dispose();
        } } );
      }
      this.$$arezi$$_state = -1;
    }
  }

  @Override
  public T count(final String param) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arezi$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arezi$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arezi$$_name() + "'" );
    }
    return this.$$arez$$_count.get( param );
  }

  @Override
  public final int hashCode() {
    return Long.hashCode( $$arezi$$_id() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_TypeParamMemoizeModel) ) {
      return false;
    } else {
      final Arez_TypeParamMemoizeModel that = (Arez_TypeParamMemoizeModel) o;;
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
