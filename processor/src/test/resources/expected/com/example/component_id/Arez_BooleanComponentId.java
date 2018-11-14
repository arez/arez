package com.example.component_id;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.component.ComponentKernel;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
public final class Arez_BooleanComponentId extends BooleanComponentId implements Disposable, Identifiable<Boolean>, DisposeTrackable {
  private final ComponentKernel $$arezi$$_kernel;

  public Arez_BooleanComponentId() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final Object $$arezv$$_id = getId();
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "BooleanComponentId." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "BooleanComponentId", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_preDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, $$arezv$$_name, 0, $$arezv$$_component, new DisposeNotifier(), Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  @Override
  @Nonnull
  public final Boolean getArezId() {
    return getId();
  }

  private void $$arezi$$_preDispose() {
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
    this.$$arezi$$_preDispose();
  }

  @Override
  public final int hashCode() {
    if ( Arez.areNativeComponentsEnabled() ) {
      return Boolean.hashCode( getId() );
    } else {
      return super.hashCode();
    }
  }

  @Override
  public final boolean equals(final Object o) {
    if ( Arez.areNativeComponentsEnabled() ) {
      if ( o instanceof Arez_BooleanComponentId ) {
        final Arez_BooleanComponentId that = (Arez_BooleanComponentId) o;
        return isDisposed() == that.isDisposed() && getId() == that.getId();
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
