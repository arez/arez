package com.example.post_construct;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
import arez.Flags;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import arez.component.internal.ComponentKernel;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
public final class Arez_NonStandardNamePostConstructModel extends NonStandardNamePostConstructModel implements Disposable, Identifiable<Integer>, DisposeTrackable {
  private static volatile int $$arezi$$_nextId;

  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final ComputableValue<Integer> $$arez$$_someValue;

  public Arez_NonStandardNamePostConstructModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ( Arez.areNamesEnabled() || Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? ++$$arezi$$_nextId : 0;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "NonStandardNamePostConstructModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "NonStandardNamePostConstructModel", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_nativeComponentPreDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, $$arezv$$_name, $$arezv$$_id, $$arezv$$_component, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arez$$_someValue = $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".someValue" : null, () -> super.someValue(), Flags.PRIORITY_NORMAL | Flags.ENVIRONMENT_NOT_REQUIRED | Flags.AREZ_DEPENDENCIES | Flags.RUN_LATER );
    this.$$arezi$$_kernel.componentConstructed();
    super.postConst$$$ruct();
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
    this.$$arez$$_someValue.dispose();
  }

  @Override
  public int someValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'someValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arez$$_someValue.get();
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
      if ( o instanceof Arez_NonStandardNamePostConstructModel ) {
        final Arez_NonStandardNamePostConstructModel that = (Arez_NonStandardNamePostConstructModel) o;
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