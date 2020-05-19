package com.example.component_id_ref;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.internal.ComponentKernel;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("rawtypes")
final class Arez_RawTypeComponentIdRefModel extends RawTypeComponentIdRefModel implements Disposable, Identifiable<RawTypeComponentIdRefModel.MyId>, DisposeNotifier {
  private final ComponentKernel $$arezi$$_kernel;

  @SuppressWarnings("rawtypes")
  Arez_RawTypeComponentIdRefModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final RawTypeComponentIdRefModel.MyId $$arezv$$_id = id();
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "RawTypeComponentIdRefModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "RawTypeComponentIdRefModel", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, 0, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, null, null, true, false, false );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  @Override
  @SuppressWarnings("rawtypes")
  @Nonnull
  RawTypeComponentIdRefModel.MyId getId() {
    return this.id();
  }

  @Override
  @Nonnull
  @SuppressWarnings("rawtypes")
  public RawTypeComponentIdRefModel.MyId getArezId() {
    return id();
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_kernel.notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(@Nonnull final Object key, @Nonnull final SafeProcedure action) {
    this.$$arezi$$_kernel.addOnDisposeListener( key, action );
  }

  @Override
  public void removeOnDisposeListener(@Nonnull final Object key) {
    this.$$arezi$$_kernel.removeOnDisposeListener( key );
  }

  @Override
  public boolean isDisposed() {
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    this.$$arezi$$_kernel.dispose();
  }

  @Override
  public int hashCode() {
    if ( Arez.areNativeComponentsEnabled() ) {
      return null != id() ? id().hashCode() : System.identityHashCode( this );
    } else {
      return super.hashCode();
    }
  }

  @Override
  public boolean equals(final Object o) {
    if ( Arez.areNativeComponentsEnabled() ) {
      if ( o instanceof Arez_RawTypeComponentIdRefModel ) {
        final Arez_RawTypeComponentIdRefModel that = (Arez_RawTypeComponentIdRefModel) o;
        return isDisposed() == that.isDisposed() && null != id() && id().equals( that.id() );
      } else {
        return false;
      }
    } else {
      return super.equals( o );
    }
  }

  @Override
  public String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + this.$$arezi$$_kernel.getName() + "]";
    } else {
      return super.toString();
    }
  }
}
