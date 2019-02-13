package com.example.type_params;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
import arez.Flags;
import arez.SafeProcedure;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import arez.component.internal.ComponentKernel;
import java.io.IOException;
import java.io.Writer;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
public final class Arez_ConcreteModel<W extends Writer> extends ConcreteModel<W> implements Disposable, Identifiable<IOException>, DisposeTrackable {
  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final ComputableValue<W> $$arez$$_compWriter;

  @Nonnull
  private final ComputableValue<IOException> $$arez$$_compError;

  public Arez_ConcreteModel(final W writer) {
    super(writer);
    final ArezContext $$arezv$$_context = Arez.context();
    final Object $$arezv$$_id = getComponentId();
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "ConcreteModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "ConcreteModel", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_nativeComponentPreDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, 0, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arez$$_compWriter = $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".compWriter" : null, () -> super.compWriter(), Flags.PRIORITY_NORMAL | Flags.AREZ_DEPENDENCIES | Flags.RUN_LATER );
    this.$$arez$$_compError = $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".compError" : null, () -> super.compError(), Flags.PRIORITY_NORMAL | Flags.AREZ_DEPENDENCIES | Flags.RUN_LATER );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  public Arez_ConcreteModel(final IOException error) {
    super(error);
    final ArezContext $$arezv$$_context = Arez.context();
    final Object $$arezv$$_id = getComponentId();
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "ConcreteModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "ConcreteModel", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_nativeComponentPreDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, 0, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arez$$_compWriter = $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".compWriter" : null, () -> super.compWriter(), Flags.PRIORITY_NORMAL | Flags.AREZ_DEPENDENCIES | Flags.RUN_LATER );
    this.$$arez$$_compError = $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".compError" : null, () -> super.compError(), Flags.PRIORITY_NORMAL | Flags.AREZ_DEPENDENCIES | Flags.RUN_LATER );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  public Arez_ConcreteModel(final IOException error, final W writer, final int i) {
    super(error,writer,i);
    final ArezContext $$arezv$$_context = Arez.context();
    final Object $$arezv$$_id = getComponentId();
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "ConcreteModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "ConcreteModel", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_nativeComponentPreDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, 0, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arez$$_compWriter = $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".compWriter" : null, () -> super.compWriter(), Flags.PRIORITY_NORMAL | Flags.AREZ_DEPENDENCIES | Flags.RUN_LATER );
    this.$$arez$$_compError = $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".compError" : null, () -> super.compError(), Flags.PRIORITY_NORMAL | Flags.AREZ_DEPENDENCIES | Flags.RUN_LATER );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  @Override
  @Nonnull
  public final IOException getArezId() {
    return getComponentId();
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

  private void $$arezi$$_dispose() {
    this.$$arez$$_compWriter.dispose();
    this.$$arez$$_compError.dispose();
  }

  @Override
  public void handleWriter(final W writer) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'handleWriter' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.getContext().safeAction(Arez.areNamesEnabled() ? this.$$arezi$$_kernel.getName() + ".handleWriter" : null, () -> super.handleWriter( writer ), Flags.READ_WRITE | Flags.VERIFY_ACTION_REQUIRED, Arez.areSpiesEnabled() ? new Object[] { writer } : null );
  }

  @Override
  public void handleError(final IOException error) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'handleError' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.getContext().safeAction(Arez.areNamesEnabled() ? this.$$arezi$$_kernel.getName() + ".handleError" : null, () -> super.handleError( error ), Flags.READ_WRITE | Flags.VERIFY_ACTION_REQUIRED, Arez.areSpiesEnabled() ? new Object[] { error } : null );
  }

  @Override
  public W genWriter() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'genWriter' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel.getContext().safeAction(Arez.areNamesEnabled() ? this.$$arezi$$_kernel.getName() + ".genWriter" : null, () -> super.genWriter(), Flags.READ_WRITE | Flags.VERIFY_ACTION_REQUIRED, null );
  }

  @Override
  public IOException genError() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'genError' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel.getContext().safeAction(Arez.areNamesEnabled() ? this.$$arezi$$_kernel.getName() + ".genError" : null, () -> super.genError(), Flags.READ_WRITE | Flags.VERIFY_ACTION_REQUIRED, null );
  }

  @Override
  public W compWriter() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'compWriter' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arez$$_compWriter.get();
  }

  @Override
  public IOException compError() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'compError' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arez$$_compError.get();
  }

  @Override
  public final int hashCode() {
    if ( Arez.areNativeComponentsEnabled() ) {
      return null != getComponentId() ? getComponentId().hashCode() : System.identityHashCode( this );
    } else {
      return super.hashCode();
    }
  }

  @Override
  public final boolean equals(final Object o) {
    if ( Arez.areNativeComponentsEnabled() ) {
      if ( o instanceof Arez_ConcreteModel ) {
        final Arez_ConcreteModel that = (Arez_ConcreteModel) o;
        return isDisposed() == that.isDisposed() && null != getComponentId() && getComponentId().equals( that.getComponentId() );
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
