package com.example.inject;

import arez.ActionFlags;
import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.component.Identifiable;
import arez.component.internal.ComponentKernel;
import java.util.concurrent.Callable;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.realityforge.braincheck.Guards;

@SuppressWarnings("rawtypes")
@Generated("arez.processor.ArezProcessor")
@Singleton
public final class Arez_FactoryConsumer10Model extends FactoryConsumer10Model implements Disposable, Identifiable<Integer> {
  private static volatile int $$arezi$$_nextId;

  private final ComponentKernel $$arezi$$_kernel;

  private Arez_FactoryConsumer10Model(final int count, final Callable action) {
    super(count,action);
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ( Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? ++$$arezi$$_nextId : 0;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "FactoryConsumer10Model" : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "FactoryConsumer10Model", $$arezv$$_id, $$arezv$$_name ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, null, null, false, false, false );
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

  @Override
  public boolean isDisposed() {
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    this.$$arezi$$_kernel.dispose();
  }

  @Override
  public void myActionStuff() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'myActionStuff' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.getContext().safeAction(Arez.areNamesEnabled() ? this.$$arezi$$_kernel.getName() + ".myActionStuff" : null, () -> super.myActionStuff(), ActionFlags.READ_WRITE | ActionFlags.VERIFY_ACTION_REQUIRED, null );
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
      if ( o instanceof Arez_FactoryConsumer10Model ) {
        final Arez_FactoryConsumer10Model that = (Arez_FactoryConsumer10Model) o;
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

  public static final class Factory {
    private final Callable action;

    @Inject
    Factory(final Callable action) {
      this.action = action;
    }

    @Nonnull
    public final Arez_FactoryConsumer10Model create(final int count) {
      return new Arez_FactoryConsumer10Model( count, action );
    }
  }
}