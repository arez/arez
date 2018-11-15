package com.example.component_id_ref;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import arez.component.internal.ComponentKernel;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
public final class Arez_NonIntTypeModel extends NonIntTypeModel implements Disposable, Identifiable<String>, DisposeTrackable {
  private final ComponentKernel $$arezi$$_kernel;

  public Arez_NonIntTypeModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final Object $$arezv$$_id = id();
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "NonIntTypeModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "NonIntTypeModel", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_nativeComponentPreDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, $$arezv$$_name, 0, $$arezv$$_component, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  @Override
  final String getId() {
    return this.id();
  }

  @Override
  @Nonnull
  public final String getArezId() {
    return id();
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
  }

  @Override
  public final int hashCode() {
    if ( Arez.areNativeComponentsEnabled() ) {
      return null != id() ? id().hashCode() : System.identityHashCode( this );
    } else {
      return super.hashCode();
    }
  }

  @Override
  public final boolean equals(final Object o) {
    if ( Arez.areNativeComponentsEnabled() ) {
      if ( o instanceof Arez_NonIntTypeModel ) {
        final Arez_NonIntTypeModel that = (Arez_NonIntTypeModel) o;
        return isDisposed() == that.isDisposed() && null != id() && id().equals( that.id() );
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
