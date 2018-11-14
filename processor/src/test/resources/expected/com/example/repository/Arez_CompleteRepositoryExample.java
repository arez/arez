package com.example.repository;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
import arez.Flags;
import arez.ObservableValue;
import arez.component.ComponentKernel;
import arez.component.ComponentObservable;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
final class Arez_CompleteRepositoryExample extends CompleteRepositoryExample implements Disposable, Identifiable<Integer>, ComponentObservable, DisposeTrackable {
  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final ObservableValue<String> $$arez$$_name;

  @Nonnull
  private final ObservableValue<String> $$arez$$_packageName;

  @Nonnull
  private final ObservableValue<String> $$arez$$_rawQualifiedName;

  @Nonnull
  private final ComputableValue<String> $$arez$$_qualifiedName;

  Arez_CompleteRepositoryExample(@Nonnull final String packageName, @Nonnull final String name) {
    super(packageName,name);
    final ArezContext $$arezv$$_context = Arez.context();
    final Object $$arezv$$_id = getId();
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "CompleteRepositoryExample." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "CompleteRepositoryExample", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_preDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, $$arezv$$_name, 0, $$arezv$$_component, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, true, true, false );
    this.$$arez$$_name = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".name" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getName() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setName( v ) : null );
    this.$$arez$$_packageName = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".packageName" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getPackageName() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setPackageName( v ) : null );
    this.$$arez$$_rawQualifiedName = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".rawQualifiedName" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getRawQualifiedName() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setQualifiedName( v ) : null );
    this.$$arez$$_qualifiedName = $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".qualifiedName" : null, () -> super.getQualifiedName(), Flags.PRIORITY_NORMAL | Flags.ENVIRONMENT_NOT_REQUIRED | Flags.AREZ_DEPENDENCIES | Flags.RUN_LATER );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  @Override
  @Nonnull
  public final Integer getArezId() {
    return getId();
  }

  @Override
  public boolean observe() {
    return this.$$arezi$$_kernel.observe();
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
    this.$$arezi$$_kernel.releaseResources();
    this.$$arez$$_qualifiedName.dispose();
    this.$$arez$$_name.dispose();
    this.$$arez$$_packageName.dispose();
    this.$$arez$$_rawQualifiedName.dispose();
  }

  @Nonnull
  @Override
  public String getName() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getName' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arez$$_name.reportObserved();
    return super.getName();
  }

  @Override
  public void setName(@Nonnull final String name) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setName' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arez$$_name.preReportChanged();
    final String $$arezv$$_currentValue = super.getName();
    assert null != name;
    if ( !Objects.equals( name, $$arezv$$_currentValue ) ) {
      super.setName( name );
      this.$$arez$$_name.reportChanged();
    }
  }

  @Nonnull
  @Override
  public String getPackageName() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getPackageName' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arez$$_packageName.reportObserved();
    return super.getPackageName();
  }

  @Override
  public void setPackageName(@Nonnull final String packageName) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setPackageName' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arez$$_packageName.preReportChanged();
    final String $$arezv$$_currentValue = super.getPackageName();
    assert null != packageName;
    if ( !Objects.equals( packageName, $$arezv$$_currentValue ) ) {
      super.setPackageName( packageName );
      this.$$arez$$_packageName.reportChanged();
    }
  }

  @Nullable
  @Override
  public String getRawQualifiedName() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getRawQualifiedName' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arez$$_rawQualifiedName.reportObserved();
    return super.getRawQualifiedName();
  }

  @Override
  public void setQualifiedName(@Nullable final String qualifiedName) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setQualifiedName' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arez$$_rawQualifiedName.preReportChanged();
    final String $$arezv$$_currentValue = super.getRawQualifiedName();
    if ( !Objects.equals( qualifiedName, $$arezv$$_currentValue ) ) {
      super.setQualifiedName( qualifiedName );
      this.$$arez$$_rawQualifiedName.reportChanged();
    }
  }

  @Nonnull
  @Override
  public String getQualifiedName() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getQualifiedName' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arez$$_qualifiedName.get();
  }

  @Override
  public final int hashCode() {
    return Integer.hashCode( getId() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( o instanceof Arez_CompleteRepositoryExample ) {
      final Arez_CompleteRepositoryExample that = (Arez_CompleteRepositoryExample) o;
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
