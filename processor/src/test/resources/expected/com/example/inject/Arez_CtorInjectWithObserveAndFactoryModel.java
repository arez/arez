package com.example.inject;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Flags;
import arez.Guards;
import arez.Observer;
import arez.component.Identifiable;
import arez.component.internal.ComponentKernel;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Singleton
public final class Arez_CtorInjectWithObserveAndFactoryModel extends CtorInjectWithObserveAndFactoryModel implements Disposable, Identifiable<Integer> {
  private static volatile int $$arezi$$_nextId;

  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final Observer $$arez$$_autorun;

  private Arez_CtorInjectWithObserveAndFactoryModel(@Nonnull final Runnable action,
      final int count) {
    super(action,count);
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ( Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? ++$$arezi$$_nextId : 0;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "CtorInjectWithObserveAndFactoryModel" : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "CtorInjectWithObserveAndFactoryModel", $$arezv$$_id, $$arezv$$_name ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, false, false, false );
    this.$$arez$$_autorun = $$arezv$$_context.observer( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".autorun" : null, () -> super.autorun(), Flags.RUN_LATER | Flags.NESTED_ACTIONS_DISALLOWED | Flags.AREZ_DEPENDENCIES );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentComplete();
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

  private void $$arezi$$_dispose() {
    this.$$arez$$_autorun.dispose();
  }

  @Override
  void autorun() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.fail( () -> "Observe method named 'autorun' invoked but @Observe(executor=INTERNAL) annotated methods should only be invoked by the runtime." );
    }
    super.autorun();
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
      if ( o instanceof Arez_CtorInjectWithObserveAndFactoryModel ) {
        final Arez_CtorInjectWithObserveAndFactoryModel that = (Arez_CtorInjectWithObserveAndFactoryModel) o;
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
    @Nonnull
    private final Runnable action;

    @Inject
    Factory(@Nonnull final Runnable action) {
      this.action = Objects.requireNonNull( action );
    }

    @Nonnull
    public final Arez_CtorInjectWithObserveAndFactoryModel create(final int count) {
      return new Arez_CtorInjectWithObserveAndFactoryModel( action, count );
    }
  }
}
