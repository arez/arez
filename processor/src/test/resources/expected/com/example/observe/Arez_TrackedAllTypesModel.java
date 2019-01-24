package com.example.observe;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Flags;
import arez.Observer;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import arez.component.internal.ComponentKernel;
import java.text.ParseException;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public final class Arez_TrackedAllTypesModel extends TrackedAllTypesModel implements Disposable, Identifiable<Integer>, DisposeTrackable {
  private static volatile int $$arezi$$_nextId;

  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final Observer $$arez$$_render1;

  @Nonnull
  private final Observer $$arez$$_render2;

  @Nonnull
  private final Observer $$arez$$_render3;

  @Nonnull
  private final Observer $$arez$$_render4;

  public Arez_TrackedAllTypesModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ( Arez.areNamesEnabled() || Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? ++$$arezi$$_nextId : 0;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "TrackedAllTypesModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "TrackedAllTypesModel", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_nativeComponentPreDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arez$$_render1 = $$arezv$$_context.tracker( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".render1" : null, () -> super.onRender1DepsChange(), Flags.RUN_LATER | Flags.NESTED_ACTIONS_DISALLOWED | Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_render2 = $$arezv$$_context.tracker( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".render2" : null, () -> super.onRender2DepsChange(), Flags.RUN_LATER | Flags.NESTED_ACTIONS_DISALLOWED | Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_render3 = $$arezv$$_context.tracker( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".render3" : null, () -> super.onRender3DepsChange(), Flags.RUN_LATER | Flags.NESTED_ACTIONS_DISALLOWED | Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_render4 = $$arezv$$_context.tracker( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".render4" : null, () -> super.onRender4DepsChange(), Flags.RUN_LATER | Flags.NESTED_ACTIONS_DISALLOWED | Flags.AREZ_DEPENDENCIES );
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
    this.$$arez$$_render1.dispose();
    this.$$arez$$_render2.dispose();
    this.$$arez$$_render3.dispose();
    this.$$arez$$_render4.dispose();
  }

  @Override
  public void render1() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'render1' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.getContext().safeObserve( this.$$arez$$_render1, () -> super.render1(), null );
  }

  @Override
  public void render2() throws ParseException {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'render2' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    try {
      this.$$arezi$$_kernel.getContext().observe( this.$$arez$$_render2, () -> super.render2(), null );
    } catch( final ParseException | RuntimeException | Error $$arez_exception$$ ) {
      throw $$arez_exception$$;
    } catch( final Throwable $$arez_exception$$ ) {
      throw new IllegalStateException( $$arez_exception$$ );
    }
  }

  @Override
  protected int render3() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'render3' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel.getContext().safeObserve( this.$$arez$$_render3, () -> super.render3(), null );
  }

  @Override
  int render4() throws ParseException {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'render4' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    try {
      return this.$$arezi$$_kernel.getContext().observe( this.$$arez$$_render4, () -> super.render4(), null );
    } catch( final ParseException | RuntimeException | Error $$arez_exception$$ ) {
      throw $$arez_exception$$;
    } catch( final Throwable $$arez_exception$$ ) {
      throw new IllegalStateException( $$arez_exception$$ );
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
      if ( o instanceof Arez_TrackedAllTypesModel ) {
        final Arez_TrackedAllTypesModel that = (Arez_TrackedAllTypesModel) o;
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
