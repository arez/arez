package com.example.inheritance.other;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Locator;
import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.Linkable;
import arez.component.Verifiable;
import arez.component.internal.ComponentKernel;
import com.example.inheritance.Arez_CompleteInterfaceModel;
import com.example.inheritance.CompleteInterfaceModel;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public final class Arez_OtherElement extends OtherElement implements Disposable, Identifiable<Integer>, Verifiable, DisposeNotifier, Linkable {
  private static volatile int $$arezi$$_nextId;

  @Nullable
  private final ComponentKernel $$arezi$$_kernel;

  @Nullable
  private CompleteInterfaceModel $$arezr$$_completeInterfaceModel;

  @Nullable
  private CompleteInterfaceModel $$arezr$$_child;

  public Arez_OtherElement() {
    super();
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( Arez::areReferencesEnabled, () -> "Attempted to create instance of component of type 'com_example_inheritance_other_OtherElement' that contains references but Arez.areReferencesEnabled() returns false. References need to be enabled to use this component" );
    }
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "com_example_inheritance_other_OtherElement." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "com_example_inheritance_other_OtherElement", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_preDispose, null, null, true, false, false );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_link_completeInterfaceModel();
    this.$$arezi$$_kernel.componentReady();
  }

  @Nonnull
  private Locator $$arezi$$_locator() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'com_example_inheritance_other_OtherElement'" );
    }
    return this.$$arezi$$_kernel.getContext().locator();
  }

  private int $$arezi$$_id() {
    assert null != this.$$arezi$$_kernel;
    return this.$$arezi$$_kernel.getId();
  }

  @Override
  @Nonnull
  public Integer getArezId() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getArezId' invoked on uninitialized component of type 'com_example_inheritance_other_OtherElement'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'getArezId' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return $$arezi$$_id();
  }

  private void $$arezi$$_preDispose() {
    this.$$arezi$$_delink_completeInterfaceModel();
    this.$$arezi$$_delink_child();
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_preDispose();
    this.$$arezi$$_kernel.notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(@Nonnull final Object key, @Nonnull final SafeProcedure action,
      final boolean errorIfDuplicate) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_inheritance_other_OtherElement'" );
    }
    this.$$arezi$$_kernel.addOnDisposeListener( key, action, errorIfDuplicate );
  }

  @Override
  public void removeOnDisposeListener(@Nonnull final Object key, final boolean errorIfMissing) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_inheritance_other_OtherElement'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.removeOnDisposeListener( key, errorIfMissing );
  }

  @Override
  public boolean isDisposed() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_inheritance_other_OtherElement'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'isDisposed' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'dispose' invoked on uninitialized component of type 'com_example_inheritance_other_OtherElement'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'dispose' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.dispose();
  }

  @Override
  public void verify() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'verify' invoked on uninitialized component of type 'com_example_inheritance_other_OtherElement'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'verify' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'verify' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() && Arez.isVerifyEnabled() ) {
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( OtherElement.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type OtherElement and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( OtherElement.class, $$arezi$$_id() ) );
      final int $$arezv$$_completeInterfaceModelId = this.getCompleteInterfaceModelId();
      final CompleteInterfaceModel $$arezv$$_completeInterfaceModel = this.$$arezi$$_locator().findById( CompleteInterfaceModel.class, $$arezv$$_completeInterfaceModelId );
      Guards.apiInvariant( () -> null != $$arezv$$_completeInterfaceModel, () -> "Reference named 'completeInterfaceModel' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inheritance.CompleteInterfaceModel and id = " + getCompleteInterfaceModelId() );
      final int $$arezv$$_childId = this.getChildId();
      final CompleteInterfaceModel $$arezv$$_child = this.$$arezi$$_locator().findById( CompleteInterfaceModel.class, $$arezv$$_childId );
      Guards.apiInvariant( () -> null != $$arezv$$_child, () -> "Reference named 'child' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inheritance.CompleteInterfaceModel and id = " + getChildId() );
    }
  }

  @Override
  public void link() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'link' invoked on uninitialized component of type 'com_example_inheritance_other_OtherElement'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'link' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'link' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arezi$$_link_child();
  }

  @Override
  CompleteInterfaceModel getCompleteInterfaceModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getCompleteInterfaceModel' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_completeInterfaceModel, () -> "Nonnull reference method named 'getCompleteInterfaceModel' invoked on component named '" + this.$$arezi$$_kernel.getName() + "' but reference has not been resolved yet is not lazy. Id = " + getCompleteInterfaceModelId() );
    }
    return this.$$arezr$$_completeInterfaceModel;
  }

  private void $$arezi$$_link_completeInterfaceModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezi$$_link_completeInterfaceModel' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    final int id = this.getCompleteInterfaceModelId();
    this.$$arezr$$_completeInterfaceModel = this.$$arezi$$_locator().findById( CompleteInterfaceModel.class, id );
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_completeInterfaceModel, () -> "Reference named 'completeInterfaceModel' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inheritance.CompleteInterfaceModel and id = " + getCompleteInterfaceModelId() );
    }
    ( (Arez_CompleteInterfaceModel) this.$$arezr$$_completeInterfaceModel ).$$arezir$$_otherElements_add( this );
  }

  public void $$arezi$$_delink_completeInterfaceModel() {
    if ( null != $$arezr$$_completeInterfaceModel && Disposable.isNotDisposed( $$arezr$$_completeInterfaceModel ) ) {
      ( (Arez_CompleteInterfaceModel) this.$$arezr$$_completeInterfaceModel ).$$arezir$$_otherElements_remove( this );
    }
    this.$$arezr$$_completeInterfaceModel = null;
  }

  @Nonnull
  @Override
  CompleteInterfaceModel getChild() {
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
      this.$$arezr$$_child = this.$$arezi$$_locator().findById( CompleteInterfaceModel.class, id );
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != $$arezr$$_child, () -> "Reference named 'child' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inheritance.CompleteInterfaceModel and id = " + getChildId() );
      }
      ( (Arez_CompleteInterfaceModel) this.$$arezr$$_child ).$$arezir$$_parentGeneralisation_zset( this );
    }
  }

  public void $$arezi$$_delink_child() {
    if ( null != $$arezr$$_child && Disposable.isNotDisposed( $$arezr$$_child ) ) {
      ( (Arez_CompleteInterfaceModel) this.$$arezr$$_child ).$$arezir$$_parentGeneralisation_zunset( this );
    }
    this.$$arezr$$_child = null;
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
