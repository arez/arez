package com.example.collections;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
import arez.Flags;
import arez.component.CollectionsUtil;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import arez.component.internal.ComponentKernel;
import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
public final class Arez_MemoizeNonnullMapModel extends MemoizeNonnullMapModel implements Disposable, Identifiable<Integer>, DisposeTrackable {
  private static volatile int $$arezi$$_nextId;

  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final ComputableValue<Map<String, String>> $$arez$$_myValue;

  private Map<String, String> $$arezd$$_$$cache$$_myValue;

  private boolean $$arezd$$_$$cache_active$$_myValue;

  public Arez_MemoizeNonnullMapModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ( Arez.areNamesEnabled() || Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? ++$$arezi$$_nextId : 0;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "MemoizeNonnullMapModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "MemoizeNonnullMapModel", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_nativeComponentPreDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arez$$_myValue = Arez.areCollectionsPropertiesUnmodifiable() ? $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myValue" : null, () -> super.getMyValue(), this::$$arezi$$_onActivate_myValue, this::$$arezi$$_onDeactivate_myValue, this::$$arezi$$_onStale_myValue, Flags.PRIORITY_NORMAL | Flags.AREZ_DEPENDENCIES | Flags.RUN_LATER ) : $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myValue" : null, () -> super.getMyValue(), Flags.PRIORITY_NORMAL | Flags.AREZ_DEPENDENCIES | Flags.RUN_LATER );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  final int $$arezi$$_id() {
    return this.$$arezi$$_kernel.getId();
  }

  @Override
  @Nonnull
  public final Integer getArezId() {
    return $$arezi$$_id();
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_kernel.getDisposeNotifier().dispose();
  }

  @Override
  @Nonnull
  public DisposeNotifier getNotifier() {
    return this.$$arezi$$_kernel.getDisposeNotifier();
  }

  @Override
  public boolean isDisposed() {
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    this.$$arezi$$_kernel.dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_myValue.dispose();
  }

  @Nonnull
  @Override
  public Map<String, String> getMyValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      if ( null == this.$$arezd$$_$$cache$$_myValue ) {
        this.$$arezd$$_$$cache$$_myValue = CollectionsUtil.wrap( (Map<String, String>) this.$$arez$$_myValue.get() );
      } else {
        // Make sure that we are observing computable value
        this.$$arez$$_myValue.get();
      }
      return $$arezd$$_$$cache$$_myValue;
    } else {
      return (Map<String, String>) this.$$arez$$_myValue.get();
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
      if ( o instanceof Arez_MemoizeNonnullMapModel ) {
        final Arez_MemoizeNonnullMapModel that = (Arez_MemoizeNonnullMapModel) o;
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
      return "ArezComponent[" + this.$$arezi$$_kernel.getName() + "]";
    } else {
      return super.toString();
    }
  }
}
