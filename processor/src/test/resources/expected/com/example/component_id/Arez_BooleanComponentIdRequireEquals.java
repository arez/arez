package com.example.component_id;

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
public final class Arez_BooleanComponentIdRequireEquals extends BooleanComponentIdRequireEquals implements Disposable, Identifiable<Boolean>, DisposeTrackable {
  private final ComponentKernel $$arezi$$_kernel;

  public Arez_BooleanComponentIdRequireEquals() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final Object $$arezv$$_id = getId();
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "BooleanComponentIdRequireEquals." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "BooleanComponentIdRequireEquals", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_nativeComponentPreDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, 0, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, null, null, true, false, false );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  @Override
  @Nonnull
  public final Boolean getArezId() {
    return getId();
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

  @Override
  public final int hashCode() {
    return Boolean.hashCode( getId() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( o instanceof Arez_BooleanComponentIdRequireEquals ) {
      final Arez_BooleanComponentIdRequireEquals that = (Arez_BooleanComponentIdRequireEquals) o;
      return isDisposed() == that.isDisposed() && getId() == that.getId();
    } else {
      return false;
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
