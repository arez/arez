package com.example.inheritance.other;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Locator;
import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import arez.component.Linkable;
import arez.component.Verifiable;
import arez.component.internal.ComponentKernel;
import com.example.inheritance.Arez_CompleteModel;
import com.example.inheritance.CompleteModel;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public final class Arez_Element extends Element implements Disposable, Verifiable, DisposeNotifier, Linkable {
  private static volatile int $$arezi$$_nextId;

  private final ComponentKernel $$arezi$$_kernel;

  @Nullable
  private CompleteModel $$arezr$$_completeModel;

  @Nullable
  private CompleteModel $$arezr$$_child;

  public Arez_Element() {
    super();
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( Arez::areReferencesEnabled, () -> "Attempted to create instance of component of type 'Element' that contains references but Arez.areReferencesEnabled() returns false. References need to be enabled to use this component" );
    }
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ( Arez.areNamesEnabled() || Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? ++$$arezi$$_nextId : 0;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "Element." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "Element", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_preDispose, null, null, true, false, false );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_link_completeModel();
    this.$$arezi$$_kernel.componentReady();
  }

  @Nonnull
  private Locator $$arezi$$_locator() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'Element'" );
    }
    return this.$$arezi$$_kernel.getContext().locator();
  }

  private int $$arezi$$_id() {
    return this.$$arezi$$_kernel.getId();
  }

  private void $$arezi$$_preDispose() {
    this.$$arezi$$_delink_completeModel();
    this.$$arezi$$_delink_child();
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

  @Override
  public void verify() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'verify' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() && Arez.isVerifyEnabled() ) {
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( Element.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type Element and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( Element.class, $$arezi$$_id() ) );
      final int $$arezv$$_completeModelId = this.getCompleteModelId();
      final CompleteModel $$arezv$$_completeModel = this.$$arezi$$_locator().findById( CompleteModel.class, $$arezv$$_completeModelId );
      Guards.apiInvariant( () -> null != $$arezv$$_completeModel, () -> "Reference named 'completeModel' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inheritance.CompleteModel and id = " + getCompleteModelId() );
      final int $$arezv$$_childId = this.getChildId();
      final CompleteModel $$arezv$$_child = this.$$arezi$$_locator().findById( CompleteModel.class, $$arezv$$_childId );
      Guards.apiInvariant( () -> null != $$arezv$$_child, () -> "Reference named 'child' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inheritance.CompleteModel and id = " + getChildId() );
    }
  }

  @Override
  public void link() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'link' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arezi$$_link_child();
  }

  @Override
  CompleteModel getCompleteModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getCompleteModel' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_completeModel, () -> "Nonnull reference method named 'getCompleteModel' invoked on component named '" + this.$$arezi$$_kernel.getName() + "' but reference has not been resolved yet is not lazy. Id = " + getCompleteModelId() );
    }
    return this.$$arezr$$_completeModel;
  }

  private void $$arezi$$_link_completeModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezi$$_link_completeModel' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    final int id = this.getCompleteModelId();
    this.$$arezr$$_completeModel = this.$$arezi$$_locator().findById( CompleteModel.class, id );
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_completeModel, () -> "Reference named 'completeModel' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inheritance.CompleteModel and id = " + getCompleteModelId() );
    }
    ( (Arez_CompleteModel) this.$$arezr$$_completeModel ).$$arezir$$_elements_add( this );
  }

  public void $$arezi$$_delink_completeModel() {
    if ( null != $$arezr$$_completeModel && Disposable.isNotDisposed( $$arezr$$_completeModel ) ) {
      ( (Arez_CompleteModel) this.$$arezr$$_completeModel ).$$arezir$$_elements_remove( this );
    }
    this.$$arezr$$_completeModel = null;
  }

  @Nonnull
  @Override
  CompleteModel getChild() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getChild' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_child, () -> "Nonnull reference method named 'getChild' invoked on component named '" + this.$$arezi$$_kernel.getName() + "' but reference has not been resolved yet is not lazy. Id = " + getChildId() );
    }
    return this.$$arezr$$_child;
  }

  private void $$arezi$$_link_child() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezi$$_link_child' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( null == this.$$arezr$$_child ) {
      final int id = this.getChildId();
      this.$$arezr$$_child = this.$$arezi$$_locator().findById( CompleteModel.class, id );
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != $$arezr$$_child, () -> "Reference named 'child' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inheritance.CompleteModel and id = " + getChildId() );
      }
      ( (Arez_CompleteModel) this.$$arezr$$_child ).$$arezir$$_parentGeneralisation_zset( this );
    }
  }

  public void $$arezi$$_delink_child() {
    if ( null != $$arezr$$_child && Disposable.isNotDisposed( $$arezr$$_child ) ) {
      ( (Arez_CompleteModel) this.$$arezr$$_child ).$$arezir$$_parentGeneralisation_zunset( this );
    }
    this.$$arezr$$_child = null;
  }

  @Override
  public int hashCode() {
    if ( Arez.areNativeComponentsEnabled() ) {
      return Integer.hashCode( $$arezi$$_id() );
    } else {
      return super.hashCode();
    }
  }

  @Override
  public boolean equals(final Object o) {
    if ( Arez.areNativeComponentsEnabled() ) {
      if ( o instanceof Arez_Element ) {
        final Arez_Element that = (Arez_Element) o;
        return $$arezi$$_id() == that.$$arezi$$_id();
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
