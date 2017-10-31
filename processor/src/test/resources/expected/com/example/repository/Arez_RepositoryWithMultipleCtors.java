package com.example.repository;

import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.braincheck.Guards;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_RepositoryWithMultipleCtors extends RepositoryWithMultipleCtors implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  private OnDispose $$arez$$_onDispose;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_name;

  Arez_RepositoryWithMultipleCtors(@Nonnull final String packageName, @Nonnull final String name) {
    super(packageName,name);
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_name = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? $$arez$$_name() + ".name" : null );
  }

  Arez_RepositoryWithMultipleCtors(@Nonnull final String name) {
    super(name);
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_name = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? $$arez$$_name() + ".name" : null );
  }

  Arez_RepositoryWithMultipleCtors() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_name = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? $$arez$$_name() + ".name" : null );
  }

  final long $$arez$$_id() {
    return this.$$arez$$_id;
  }

  String $$arez$$_name() {
    return "RepositoryWithMultipleCtors." + $$arez$$_id();
  }

  @Override
  public boolean isDisposed() {
    return this.$$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      this.$$arez$$_disposed = true;
      this.$$arez$$_context.safeAction( Arez.areNamesEnabled() ? $$arez$$_name() + ".dispose" : null, () -> { {
        if ( null != this.$$arez$$_onDispose ) {
          this.$$arez$$_onDispose.onDispose( this );
          this.$$arez$$_onDispose = null;
        }
        this.$$arez$$_name.dispose();
      } } );
    }
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
    } else if ( null == o || !(o instanceof Arez_RepositoryWithMultipleCtors) ) {
      return false;
    } else {
      final Arez_RepositoryWithMultipleCtors that = (Arez_RepositoryWithMultipleCtors) o;;
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
    void onDispose(Arez_RepositoryWithMultipleCtors entity);
  }
}
