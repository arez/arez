package com.example.component_id;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.braincheck.Guards;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_ComponentIdOnSingletonModel extends ComponentIdOnSingletonModel implements Disposable {
  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_field;

  public Arez_ComponentIdOnSingletonModel() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_field = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? $$arez$$_name() + ".field" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getField() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setField( v ) : null );
  }

  String $$arez$$_name() {
    return "ComponentIdOnSingletonModel";
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
        this.$$arez$$_field.dispose();
      } } );
    }
  }

  @Override
  public long getField() {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    this.$$arez$$_field.reportObserved();
    return super.getField();
  }

  @Override
  public void setField(final long field) {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    if ( field != super.getField() ) {
      super.setField(field);
      this.$$arez$$_field.reportChanged();
    }
  }

  @Override
  public final int hashCode() {
    return Long.hashCode( getId() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_ComponentIdOnSingletonModel) ) {
      return false;
    } else {
      final Arez_ComponentIdOnSingletonModel that = (Arez_ComponentIdOnSingletonModel) o;;
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
