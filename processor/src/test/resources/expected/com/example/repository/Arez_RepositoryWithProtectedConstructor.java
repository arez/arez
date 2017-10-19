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
public final class Arez_RepositoryWithProtectedConstructor extends RepositoryWithProtectedConstructor implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_name;

  protected Arez_RepositoryWithProtectedConstructor(@Nonnull final String name) {
    super(name);
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_name = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".name" : null );
  }

  final long $$arez$$_id() {
    return $$arez$$_id;
  }

  String $$arez$$_name() {
    return "RepositoryWithProtectedConstructor." + $$arez$$_id();
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      $$arez$$_name.dispose();
    }
  }

  @Nonnull
  @Override
  public String getName() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    this.$$arez$$_name.reportObserved();
    return super.getName();
  }

  @Override
  public void setName(@Nonnull final String name) {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
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
    } else if ( null == o || !(o instanceof Arez_RepositoryWithProtectedConstructor) ) {
      return false;
    } else {
      final Arez_RepositoryWithProtectedConstructor that = (Arez_RepositoryWithProtectedConstructor) o;;
      return $$arez$$_id() == that.$$arez$$_id();
    }
  }

  @Override
  public final String toString() {
    if ( $$arez$$_context.areNamesEnabled() ) {
      return "ArezComponent[" + $$arez$$_name() + "]";
    } else {
      return super.toString();
    }
  }
}
