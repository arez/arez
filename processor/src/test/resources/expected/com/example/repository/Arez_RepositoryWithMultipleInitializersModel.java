package com.example.repository;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Observable;
import arez.component.ComponentObservable;
import arez.component.ComponentState;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public final class Arez_RepositoryWithMultipleInitializersModel extends RepositoryWithMultipleInitializersModel implements Disposable, Identifiable<Integer>, ComponentObservable, DisposeTrackable {
  private static volatile int $$arezi$$_nextId;

  private final int $$arezi$$_id;

  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final Observable<Boolean> $$arezi$$_disposedObservable;

  private final DisposeNotifier $$arezi$$_disposeNotifier;

  @Nonnull
  private final Observable<Long> $$arez$$_time;

  private long $$arezd$$_time;

  @Nonnull
  private final Observable<Long> $$arez$$_value;

  private long $$arezd$$_value;

  public Arez_RepositoryWithMultipleInitializersModel(final long time, final long value) {
    super();
    this.$$arezd$$_time = time;
    this.$$arezd$$_value = value;
    this.$$arezi$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arezi$$_id = $$arezi$$_nextId++;
    if ( Arez.shouldCheckInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_INITIALIZED;
    }
    this.$$arezi$$_component = Arez.areNativeComponentsEnabled() ? $$arezi$$_context().component( "RepositoryWithMultipleInitializersModel", $$arezi$$_id(), Arez.areNamesEnabled() ? $$arezi$$_name() : null, () -> $$arezi$$_preDispose() ) : null;
    this.$$arezi$$_disposedObservable = $$arezi$$_context().observable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezi$$_state >= 0 : null );
    this.$$arezi$$_disposeNotifier = new DisposeNotifier();
    this.$$arez$$_time = $$arezi$$_context().observable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".time" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezd$$_time : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_time = v : null );
    this.$$arez$$_value = $$arezi$$_context().observable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".value" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezd$$_value : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_value = v : null );
    if ( Arez.shouldCheckInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_CONSTRUCTED;
    }
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arezi$$_component.complete();
    }
    if ( Arez.shouldCheckInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_READY;
    }
  }

  final ArezContext $$arezi$$_context() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_context' invoked on uninitialized component of type 'RepositoryWithMultipleInitializersModel'" );
    }
    return Arez.areZonesEnabled() ? this.$$arezi$$_context : Arez.context();
  }

  final int $$arezi$$_id() {
    return this.$$arezi$$_id;
  }

  @Override
  @Nonnull
  public final Integer getArezId() {
    return $$arezi$$_id();
  }

  String $$arezi$$_name() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_name' invoked on uninitialized component of type 'RepositoryWithMultipleInitializersModel'" );
    }
    return "RepositoryWithMultipleInitializersModel." + $$arezi$$_id();
  }

  private boolean $$arezi$$_observe() {
    final boolean isNotDisposed = isNotDisposed();
    if ( isNotDisposed )  {
      this.$$arezi$$_disposedObservable.reportObserved();
    }
    return isNotDisposed;
  }

  @Override
  public boolean observe() {
    return $$arezi$$_observe();
  }

  private void $$arezi$$_preDispose() {
    $$arezi$$_disposeNotifier.dispose();
  }

  @Override
  @Nonnull
  public DisposeNotifier getNotifier() {
    return $$arezi$$_disposeNotifier;
  }

  @Override
  public boolean isDisposed() {
    return ComponentState.isDisposingOrDisposed( this.$$arezi$$_state );
  }

  @Override
  public void dispose() {
    if ( !ComponentState.isDisposingOrDisposed( this.$$arezi$$_state ) ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSING;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arezi$$_component.dispose();
      } else {
        $$arezi$$_context().safeAction( Arez.areNamesEnabled() ? $$arezi$$_name() + ".dispose" : null, () -> { {
          this.$$arezi$$_preDispose();
          this.$$arezi$$_disposedObservable.dispose();
          this.$$arez$$_time.dispose();
          this.$$arez$$_value.dispose();
        } } );
      }
      if ( Arez.shouldCheckInvariants() ) {
        this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSED;
      }
    }
  }

  @Override
  public long getTime() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getTime' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_time.reportObserved();
    return this.$$arezd$$_time;
  }

  @Override
  public void setTime(final long value) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'setTime' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    final long $$arezv$$_currentValue = this.$$arezd$$_time;
    if ( value != $$arezv$$_currentValue ) {
      this.$$arez$$_time.preReportChanged();
      this.$$arezd$$_time = value;
      this.$$arez$$_time.reportChanged();
    }
  }

  @Override
  public long getValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getValue' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_value.reportObserved();
    return this.$$arezd$$_value;
  }

  @Override
  public void setValue(final long value) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'setValue' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    final long $$arezv$$_currentValue = this.$$arezd$$_value;
    if ( value != $$arezv$$_currentValue ) {
      this.$$arez$$_value.preReportChanged();
      this.$$arezd$$_value = value;
      this.$$arez$$_value.reportChanged();
    }
  }

  @Override
  public final int hashCode() {
    return Integer.hashCode( $$arezi$$_id() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_RepositoryWithMultipleInitializersModel) ) {
      return false;
    } else {
      final Arez_RepositoryWithMultipleInitializersModel that = (Arez_RepositoryWithMultipleInitializersModel) o;;
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
