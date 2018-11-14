package com.example.component_id;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.ObservableValue;
import arez.component.ComponentKernel;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public final class Arez_ComponentIdOnSingletonModel extends ComponentIdOnSingletonModel implements Disposable, Identifiable<Long>, DisposeTrackable {
  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final ObservableValue<Long> $$arez$$_field;

  public Arez_ComponentIdOnSingletonModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final Object $$arezv$$_id = getId();
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "ComponentIdOnSingletonModel" : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "ComponentIdOnSingletonModel", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_preDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, $$arezv$$_name, 0, $$arezv$$_component, new DisposeNotifier(), Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose );
    this.$$arez$$_field = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".field" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getField() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setField( v ) : null );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  @Override
  @Nonnull
  public final Long getArezId() {
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
    this.$$arez$$_field.dispose();
  }

  @Override
  public long getField() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getField' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arez$$_field.reportObserved();
    return super.getField();
  }

  @Override
  public void setField(final long field) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setField' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arez$$_field.preReportChanged();
    final long $$arezv$$_currentValue = super.getField();
    if ( field != $$arezv$$_currentValue ) {
      super.setField( field );
      this.$$arez$$_field.reportChanged();
    }
  }

  @Override
  public final int hashCode() {
    if ( Arez.areNativeComponentsEnabled() ) {
      return Long.hashCode( getId() );
    } else {
      return super.hashCode();
    }
  }

  @Override
  public final boolean equals(final Object o) {
    if ( Arez.areNativeComponentsEnabled() ) {
      if ( o instanceof Arez_ComponentIdOnSingletonModel ) {
        final Arez_ComponentIdOnSingletonModel that = (Arez_ComponentIdOnSingletonModel) o;
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
