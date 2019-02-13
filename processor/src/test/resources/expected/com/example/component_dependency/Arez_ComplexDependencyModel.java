package com.example.component_dependency;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.ObservableValue;
import arez.SafeProcedure;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import arez.component.internal.ComponentKernel;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
final class Arez_ComplexDependencyModel extends ComplexDependencyModel implements Disposable, Identifiable<Integer>, DisposeTrackable {
  private static volatile int $$arezi$$_nextId;

  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final ObservableValue<DisposeTrackable> $$arez$$_value3;

  Arez_ComplexDependencyModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ( Arez.areNamesEnabled() || Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? ++$$arezi$$_nextId : 0;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "ComplexDependencyModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "ComplexDependencyModel", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_nativeComponentPreDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_preDispose, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arez$$_value3 = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".value3" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getValue3() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setValue3( v ) : null );
    final DisposeTrackable $$arezv$$_getValue1_dependency = super.getValue1();
    if ( null != $$arezv$$_getValue1_dependency ) {
      DisposeTrackable.asDisposeTrackable( super.getValue1() ).addOnDisposeListener( this, this::dispose );
    }
    final DisposeTrackable $$arezv$$_getValue3_dependency = super.getValue3();
    if ( null != $$arezv$$_getValue3_dependency ) {
      DisposeTrackable.asDisposeTrackable( super.getValue3() ).addOnDisposeListener( this, () -> setValue3( null ) );
    }
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

  private void $$arezi$$_preDispose() {
    final DisposeTrackable $$arezv$$_getValue1_dependency = super.getValue1();
    if ( null != $$arezv$$_getValue1_dependency ) {
      DisposeTrackable.asDisposeTrackable( $$arezv$$_getValue1_dependency ).removeOnDisposeListener( this );
    }
    final DisposeTrackable $$arezv$$_getValue3_dependency = super.getValue3();
    if ( null != $$arezv$$_getValue3_dependency ) {
      DisposeTrackable.asDisposeTrackable( $$arezv$$_getValue3_dependency ).removeOnDisposeListener( this );
    }
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_preDispose();
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
    this.$$arez$$_value3.dispose();
  }

  @Override
  DisposeTrackable getValue3() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getValue3' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arez$$_value3.reportObserved();
    return super.getValue3();
  }

  @Override
  void setValue3(final DisposeTrackable value) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setValue3' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arez$$_value3.preReportChanged();
    final DisposeTrackable $$arezv$$_currentValue = super.getValue3();
    if ( !Objects.equals( value, $$arezv$$_currentValue ) ) {
      if ( null != $$arezv$$_currentValue ) {
        DisposeTrackable.asDisposeTrackable( $$arezv$$_currentValue ).removeOnDisposeListener( this );
      }
      super.setValue3( value );
      if ( null != value ) {
        DisposeTrackable.asDisposeTrackable( value ).addOnDisposeListener( this, () -> setValue3( null ) );
      }
      this.$$arez$$_value3.reportChanged();
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
      if ( o instanceof Arez_ComplexDependencyModel ) {
        final Arez_ComplexDependencyModel that = (Arez_ComplexDependencyModel) o;
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
