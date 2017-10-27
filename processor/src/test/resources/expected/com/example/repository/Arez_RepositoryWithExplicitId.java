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
public final class Arez_RepositoryWithExplicitId extends RepositoryWithExplicitId implements Disposable {
  private boolean $$arez$$_disposed;

  private OnDispose $$arez$$_onDispose;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_name;

  Arez_RepositoryWithExplicitId(@Nonnull final String packageName, @Nonnull final String name) {
    super(packageName,name);
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_name = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? $$arez$$_name() + ".name" : null );
  }

  String $$arez$$_name() {
    return "RepositoryWithExplicitId." + getId();
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      this.$$arez$$_context.safeAction( Arez.areNamesEnabled() ? $$arez$$_name() + ".dispose" : null, () -> { {
        if ( null != $$arez$$_onDispose ) {
          $$arez$$_onDispose.onDispose( this );
          $$arez$$_onDispose = null;
        }
        $$arez$$_name.dispose();
      } } );
    }
  }

  void $$arez$$_setOnDispose(OnDispose onDispose) {
    $$arez$$_onDispose = onDispose;
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
    return Integer.hashCode( getId() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_RepositoryWithExplicitId) ) {
      return false;
    } else {
      final Arez_RepositoryWithExplicitId that = (Arez_RepositoryWithExplicitId) o;;
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

  @FunctionalInterface
  interface OnDispose {
    void onDispose(Arez_RepositoryWithExplicitId entity);
  }
}
