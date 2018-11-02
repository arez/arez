package com.example.collections;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
import arez.Flags;
import arez.component.CollectionsUtil;
import arez.component.ComponentState;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import java.util.Collection;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
public final class Arez_MemoizeCollectionModel extends MemoizeCollectionModel implements Disposable, Identifiable<Integer>, DisposeTrackable {
  private static volatile int $$arezi$$_nextId;

  private final int $$arezi$$_id;

  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final DisposeNotifier $$arezi$$_disposeNotifier;

  @Nonnull
  private final ComputableValue<Collection<String>> $$arez$$_myValue;

  private Collection<String> $$arezd$$_$$cache$$_myValue;

  private boolean $$arezd$$_$$cache_active$$_myValue;

  public Arez_MemoizeCollectionModel() {
    super();
    this.$$arezi$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arezi$$_id = ( Arez.areNamesEnabled() || Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? $$arezi$$_nextId++ : 0;
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_INITIALIZED;
    }
    this.$$arezi$$_component = Arez.areNativeComponentsEnabled() ? $$arezi$$_context().component( "MemoizeCollectionModel", $$arezi$$_id(), Arez.areNamesEnabled() ? $$arezi$$_name() : null, () -> $$arezi$$_preDispose() ) : null;
    this.$$arezi$$_disposeNotifier = new DisposeNotifier();
    this.$$arez$$_myValue = Arez.areCollectionsPropertiesUnmodifiable() ? $$arezi$$_context().computable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".myValue" : null, () -> super.getMyValue(), this::$$arezi$$_onActivate_myValue, this::$$arezi$$_onDeactivate_myValue, this::$$arezi$$_onStale_myValue, Flags.PRIORITY_NORMAL | Flags.ENVIRONMENT_NOT_REQUIRED | Flags.AREZ_DEPENDENCIES | Flags.RUN_LATER ) : $$arezi$$_context().computable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".myValue" : null, () -> super.getMyValue(), Flags.PRIORITY_NORMAL | Flags.ENVIRONMENT_NOT_REQUIRED | Flags.AREZ_DEPENDENCIES | Flags.RUN_LATER );
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_CONSTRUCTED;
    }
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arezi$$_component.complete();
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_READY;
    }
  }

  final ArezContext $$arezi$$_context() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_context' invoked on uninitialized component of type 'MemoizeCollectionModel'" );
    }
    return Arez.areZonesEnabled() ? this.$$arezi$$_context : Arez.context();
  }

  final int $$arezi$$_id() {
    if ( Arez.shouldCheckInvariants() && !Arez.areNamesEnabled() && !Arez.areRegistriesEnabled() && !Arez.areNativeComponentsEnabled() ) {
      Guards.fail( () -> "Method invoked to access id when id not expected on component named '" + $$arezi$$_name() + "'." );
    }
    return this.$$arezi$$_id;
  }

  @Override
  @Nonnull
  public final Integer getArezId() {
    return $$arezi$$_id();
  }

  String $$arezi$$_name() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_name' invoked on uninitialized component of type 'MemoizeCollectionModel'" );
    }
    return "MemoizeCollectionModel." + $$arezi$$_id();
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
          this.$$arez$$_myValue.dispose();
        } }, Flags.NO_VERIFY_ACTION_REQUIRED );
      }
      if ( Arez.shouldCheckApiInvariants() ) {
        this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSED;
      }
    }
  }

  @Override
  public Collection<String> getMyValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getMyValue' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      final Collection<String> $$ar$$_result = (Collection<String>) this.$$arez$$_myValue.get();
      if ( null == this.$$arezd$$_$$cache$$_myValue && null != $$ar$$_result ) {
        this.$$arezd$$_$$cache$$_myValue = CollectionsUtil.wrap( $$ar$$_result );
      }
      return $$arezd$$_$$cache$$_myValue;
    } else {
      return (Collection<String>) this.$$arez$$_myValue.get();
    }
  }

  private void $$arezi$$_onActivate_myValue() {
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache_active$$_myValue = true;
      this.$$arezd$$_$$cache$$_myValue = null;
    }
  }

  private void $$arezi$$_onDeactivate_myValue() {
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache_active$$_myValue = false;
      this.$$arezd$$_$$cache$$_myValue = null;
    }
  }

  private void $$arezi$$_onStale_myValue() {
    if ( Arez.areCollectionsPropertiesUnmodifiable() && this.$$arezd$$_$$cache_active$$_myValue ) {
      this.$$arezd$$_$$cache$$_myValue = null;
    }
  }

  @Override
  public final int hashCode() {
    if ( Arez.areNativeComponentsEnabled() ) {
      return Integer.hashCode( $$arezi$$_id() );
    } else {
      return super.hashCode();
    }
  }

  @Override
  public final boolean equals(final Object o) {
    if ( Arez.areNativeComponentsEnabled() ) {
      if ( o instanceof Arez_MemoizeCollectionModel ) {
        final Arez_MemoizeCollectionModel that = (Arez_MemoizeCollectionModel) o;
        return $$arezi$$_id() == that.$$arezi$$_id();
      } else {
        return false;
      }
    } else {
      return super.equals( o );
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
