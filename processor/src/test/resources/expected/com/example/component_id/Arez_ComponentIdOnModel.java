package com.example.component_id;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Observable;
import arez.component.Identifiable;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public final class Arez_ComponentIdOnModel extends ComponentIdOnModel implements Disposable, Identifiable<Integer> {
  private byte $$arez$$_state;

  @Nullable
  private final ArezContext $$arez$$_context;

  private final Component $$arez$$_component;

  private final Observable<Boolean> $$arez$$_disposedObservable;

  @Nonnull
  private final Observable<Long> $$arez$$_time;

  public Arez_ComponentIdOnModel() {
    super();
    this.$$arez$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arez$$_state = 1;
    this.$$arez$$_component = Arez.areNativeComponentsEnabled() ? $$arez$$_context().createComponent( "ComponentIdOnModel", getId(), $$arez$$_name(), null, null ) : null;
    this.$$arez$$_disposedObservable = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arez$$_state >= 0 : null, null );
    this.$$arez$$_time = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".time" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getTime() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setTime( v ) : null );
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arez$$_component.complete();
    }
    this.$$arez$$_state = 2;
    this.$$arez$$_state = 3;
  }

  final ArezContext $$arez$$_context() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state == 0, () -> "Method invoked on uninitialized component named '" + $$arez$$_name() + "'" );
    }
    return Arez.areZonesEnabled() ? this.$$arez$$_context : Arez.context();
  }

  @Override
  @Nonnull
  public final Integer getArezId() {
    return getId();
  }

  String $$arez$$_name() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state == 0, () -> "Method invoked on uninitialized component named '" + $$arez$$_name() + "'" );
    }
    return "ComponentIdOnModel." + getId();
  }

  @Override
  public boolean isDisposed() {
    if ( $$arez$$_context().isTransactionActive() && !this.$$arez$$_disposedObservable.isDisposed() )  {
      this.$$arez$$_disposedObservable.reportObserved();
    }
    return this.$$arez$$_state < 0;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      this.$$arez$$_state = -2;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arez$$_component.dispose();
      } else {
        $$arez$$_context().safeAction( Arez.areNamesEnabled() ? $$arez$$_name() + ".dispose" : null, () -> { {
          this.$$arez$$_disposedObservable.dispose();
          this.$$arez$$_time.dispose();
        } } );
      }
      this.$$arez$$_state = -1;
    }
  }

  @Override
  public long getTime() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
    }
    this.$$arez$$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime(final long time) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
    }
    if ( time != super.getTime() ) {
      this.$$arez$$_time.preReportChanged();
      super.setTime(time);
      this.$$arez$$_time.reportChanged();
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
    } else if ( null == o || !(o instanceof Arez_ComponentIdOnModel) ) {
      return false;
    } else {
      final Arez_ComponentIdOnModel that = (Arez_ComponentIdOnModel) o;;
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
