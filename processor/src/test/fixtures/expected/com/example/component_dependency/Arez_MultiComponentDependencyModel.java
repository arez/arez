package com.example.component_dependency;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.ObservableValue;
import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.internal.ComponentKernel;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
final class Arez_MultiComponentDependencyModel extends MultiComponentDependencyModel implements Disposable, Identifiable<Integer>, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  @Nullable
  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final ObservableValue<MultiComponentDependencyModel.Foo> $$arez$$_foo4;

  private MultiComponentDependencyModel.Foo $$arezd$$_foo4;

  @Nonnull
  private final ObservableValue<MultiComponentDependencyModel.Foo> $$arez$$_foo5;

  private MultiComponentDependencyModel.Foo $$arezd$$_foo5;

  @Nonnull
  private final String $$arez_dk$$_getFoo;

  @Nonnull
  private final String $$arez_dk$$_getFoo2;

  @Nonnull
  private final String $$arez_dk$$_getFoo3;

  Arez_MultiComponentDependencyModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "com_example_component_dependency_MultiComponentDependencyModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "com_example_component_dependency_MultiComponentDependencyModel", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_preDispose, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arez$$_foo4 = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".foo4" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_foo4 ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_foo4 = v : null );
    this.$$arez$$_foo5 = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".foo5" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_foo5 ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_foo5 = v : null );
    this.$$arez_dk$$_getFoo = Arez_MultiComponentDependencyModel.class.getName() + $$arezv$$_id + '.' + "getFoo";
    this.$$arez_dk$$_getFoo2 = Arez_MultiComponentDependencyModel.class.getName() + $$arezv$$_id + '.' + "getFoo2";
    this.$$arez_dk$$_getFoo3 = Arez_MultiComponentDependencyModel.class.getName() + $$arezv$$_id + '.' + "getFoo3";
    final MultiComponentDependencyModel.Foo $$arezv$$_getFoo_dependency = super.getFoo();
    if ( null != $$arezv$$_getFoo_dependency ) {
      DisposeNotifier.asDisposeNotifier( super.getFoo() ).addOnDisposeListener( $$arez_dk$$_getFoo, this::dispose, true );
    }
    final MultiComponentDependencyModel.Foo $$arezv$$_getFoo2_dependency = super.getFoo2();
    if ( null != $$arezv$$_getFoo2_dependency ) {
      DisposeNotifier.asDisposeNotifier( super.getFoo2() ).addOnDisposeListener( $$arez_dk$$_getFoo2, this::dispose, true );
    }
    final MultiComponentDependencyModel.Foo $$arezv$$_getFoo3_dependency = super.getFoo3();
    if ( null != $$arezv$$_getFoo3_dependency ) {
      DisposeNotifier.asDisposeNotifier( super.getFoo3() ).addOnDisposeListener( $$arez_dk$$_getFoo3, this::dispose, true );
    }
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentComplete();
  }

  private int $$arezi$$_id() {
    assert null != this.$$arezi$$_kernel;
    return this.$$arezi$$_kernel.getId();
  }

  @Override
  @Nonnull
  public Integer getArezId() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getArezId' invoked on uninitialized component of type 'com_example_component_dependency_MultiComponentDependencyModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'getArezId' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return $$arezi$$_id();
  }

  private void $$arezi$$_preDispose() {
    final MultiComponentDependencyModel.Foo $$arezv$$_getFoo_dependency = super.getFoo();
    if ( null != $$arezv$$_getFoo_dependency ) {
      DisposeNotifier.asDisposeNotifier( $$arezv$$_getFoo_dependency ).removeOnDisposeListener( $$arez_dk$$_getFoo, true );
    }
    final MultiComponentDependencyModel.Foo $$arezv$$_getFoo2_dependency = super.getFoo2();
    if ( null != $$arezv$$_getFoo2_dependency ) {
      DisposeNotifier.asDisposeNotifier( $$arezv$$_getFoo2_dependency ).removeOnDisposeListener( $$arez_dk$$_getFoo2, true );
    }
    final MultiComponentDependencyModel.Foo $$arezv$$_getFoo3_dependency = super.getFoo3();
    if ( null != $$arezv$$_getFoo3_dependency ) {
      DisposeNotifier.asDisposeNotifier( $$arezv$$_getFoo3_dependency ).removeOnDisposeListener( $$arez_dk$$_getFoo3, true );
    }
    final MultiComponentDependencyModel.Foo $$arezv$$_getFoo4_dependency = this.$$arezd$$_foo4;
    if ( null != $$arezv$$_getFoo4_dependency ) {
      DisposeNotifier.asDisposeNotifier( $$arezv$$_getFoo4_dependency ).removeOnDisposeListener( $$arez$$_foo4, true );
    }
    final MultiComponentDependencyModel.Foo $$arezv$$_getFoo5_dependency = this.$$arezd$$_foo5;
    if ( null != $$arezv$$_getFoo5_dependency ) {
      DisposeNotifier.asDisposeNotifier( $$arezv$$_getFoo5_dependency ).removeOnDisposeListener( $$arez$$_foo5, true );
    }
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_preDispose();
    this.$$arezi$$_kernel.notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(@Nonnull final Object key, @Nonnull final SafeProcedure action,
      final boolean errorIfDuplicate) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_component_dependency_MultiComponentDependencyModel'" );
    }
    this.$$arezi$$_kernel.addOnDisposeListener( key, action, errorIfDuplicate );
  }

  @Override
  public void removeOnDisposeListener(@Nonnull final Object key, final boolean errorIfMissing) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_component_dependency_MultiComponentDependencyModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.removeOnDisposeListener( key, errorIfMissing );
  }

  @Override
  public boolean isDisposed() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_component_dependency_MultiComponentDependencyModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'isDisposed' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'dispose' invoked on uninitialized component of type 'com_example_component_dependency_MultiComponentDependencyModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'dispose' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_foo4.dispose();
    this.$$arez$$_foo5.dispose();
  }

  @Override
  MultiComponentDependencyModel.Foo getFoo4() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getFoo4' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_foo4.reportObserved();
    return this.$$arezd$$_foo4;
  }

  @Override
  void setFoo4(final MultiComponentDependencyModel.Foo foo) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setFoo4' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_foo4.preReportChanged();
    final MultiComponentDependencyModel.Foo $$arezv$$_currentValue = this.$$arezd$$_foo4;
    if ( !Objects.equals( foo, $$arezv$$_currentValue ) ) {
      if ( null != $$arezv$$_currentValue ) {
        DisposeNotifier.asDisposeNotifier( $$arezv$$_currentValue ).removeOnDisposeListener( $$arez$$_foo4, true );
      }
      this.$$arezd$$_foo4 = foo;
      if ( null != foo ) {
        DisposeNotifier.asDisposeNotifier( foo ).addOnDisposeListener( $$arez$$_foo4, this::dispose, true );
      }
      this.$$arez$$_foo4.reportChanged();
    }
  }

  @Override
  MultiComponentDependencyModel.Foo getFoo5() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getFoo5' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_foo5.reportObserved();
    return this.$$arezd$$_foo5;
  }

  @Override
  void setFoo5(final MultiComponentDependencyModel.Foo foo) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setFoo5' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_foo5.preReportChanged();
    final MultiComponentDependencyModel.Foo $$arezv$$_currentValue = this.$$arezd$$_foo5;
    if ( !Objects.equals( foo, $$arezv$$_currentValue ) ) {
      if ( null != $$arezv$$_currentValue ) {
        DisposeNotifier.asDisposeNotifier( $$arezv$$_currentValue ).removeOnDisposeListener( $$arez$$_foo5, true );
      }
      this.$$arezd$$_foo5 = foo;
      if ( null != foo ) {
        DisposeNotifier.asDisposeNotifier( foo ).addOnDisposeListener( $$arez$$_foo5, this::dispose, true );
      }
      this.$$arez$$_foo5.reportChanged();
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
