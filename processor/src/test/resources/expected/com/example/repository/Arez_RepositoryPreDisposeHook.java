package com.example.repository;

import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Component;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.braincheck.Guards;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_RepositoryPreDisposeHook extends RepositoryPreDisposeHook implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  private OnDispose $$arez$$_onDispose;

  @Nonnull
  private final ArezContext $$arez$$_context;

  private final Component $$arez$$_component;

  @Nonnull
  private final Observable $$arez$$_name;

  Arez_RepositoryPreDisposeHook(@Nonnull final String name) {
    super(name);
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_component = Arez.areNativeComponentsEnabled() ? this.$$arez$$_context.createComponent( "RepositoryPreDisposeHook", $$arez$$_id(), $$arez$$_name(), () -> $$arez$$_preDispose(), () -> super.myPostDispose() ) : null;
    this.$$arez$$_name = this.$$arez$$_context.createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".name" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getName() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setName( v ) : null );
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arez$$_component.complete();
    }
  }

  final long $$arez$$_id() {
    return this.$$arez$$_id;
  }

  String $$arez$$_name() {
    return "RepositoryPreDisposeHook." + $$arez$$_id();
  }

  @Override
  public boolean isDisposed() {
    return this.$$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      this.$$arez$$_disposed = true;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arez$$_component.dispose();
      } else {
        this.$$arez$$_context.safeAction( Arez.areNamesEnabled() ? $$arez$$_name() + ".dispose" : null, () -> { {
          $$arez$$_preDispose();
          this.$$arez$$_name.dispose();
          super.myPostDispose();
        } } );
      }
    }
  }

  void $$arez$$_preDispose() {
    if ( null != this.$$arez$$_onDispose ) {
      this.$$arez$$_onDispose.onDispose( this );
      this.$$arez$$_onDispose = null;
    }
    super.myPreDispose();
  }

  void $$arez$$_setOnDispose(OnDispose onDispose) {
    this.$$arez$$_onDispose = onDispose;
  }

  @Nonnull
  @Override
  public String getName() {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    this.$$arez$$_name.reportObserved();
    return super.getName();
  }

  @Override
  public void setName(@Nonnull final String name) {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    if ( !Objects.equals(name, super.getName()) ) {
      super.setName(name);
      this.$$arez$$_name.reportChanged();
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
    } else if ( null == o || !(o instanceof Arez_RepositoryPreDisposeHook) ) {
      return false;
    } else {
      final Arez_RepositoryPreDisposeHook that = (Arez_RepositoryPreDisposeHook) o;;
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

  @FunctionalInterface
  interface OnDispose {
    void onDispose(Arez_RepositoryPreDisposeHook entity);
  }
}
