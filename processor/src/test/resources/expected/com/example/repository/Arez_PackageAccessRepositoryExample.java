package com.example.repository;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputedValue;
import arez.Disposable;
import arez.Observable;
import arez.component.ComponentObservable;
import arez.component.ComponentState;
import arez.component.Identifiable;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
final class Arez_PackageAccessRepositoryExample extends PackageAccessRepositoryExample implements Disposable, Identifiable<Integer>, ComponentObservable {
  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final Observable<Boolean> $$arezi$$_disposedObservable;

  @Nonnull
  private final Observable<String> $$arez$$_name;

  @Nonnull
  private final Observable<String> $$arez$$_packageName;

  @Nonnull
  private final Observable<String> $$arez$$_rawQualifiedName;

  @Nonnull
  private final ComputedValue<String> $$arez$$_qualifiedName;

  Arez_PackageAccessRepositoryExample(@Nonnull final String packageName,
      @Nonnull final String name) {
    super(packageName,name);
    this.$$arezi$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arezi$$_state = ComponentState.COMPONENT_INITIALIZED;
    this.$$arezi$$_component = Arez.areNativeComponentsEnabled() ? $$arezi$$_context().createComponent( "PackageAccessRepositoryExample", getId(), $$arezi$$_name(), null, null ) : null;
    this.$$arezi$$_disposedObservable = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezi$$_state >= 0 : null, null );
    this.$$arez$$_name = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".name" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getName() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setName( v ) : null );
    this.$$arez$$_packageName = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".packageName" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getPackageName() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setPackageName( v ) : null );
    this.$$arez$$_rawQualifiedName = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".rawQualifiedName" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getRawQualifiedName() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setQualifiedName( v ) : null );
    this.$$arez$$_qualifiedName = $$arezi$$_context().createComputedValue( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".qualifiedName" : null, super::getQualifiedName, Objects::equals, null, null, null, null );
    this.$$arezi$$_state = ComponentState.COMPONENT_CONSTRUCTED;
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arezi$$_component.complete();
    }
    this.$$arezi$$_state = ComponentState.COMPONENT_READY;
  }

  final ArezContext $$arezi$$_context() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method invoked on uninitialized component of type 'PackageAccessRepositoryExample'" );
    }
    return Arez.areZonesEnabled() ? this.$$arezi$$_context : Arez.context();
  }

  @Override
  @Nonnull
  public final Integer getArezId() {
    return getId();
  }

  String $$arezi$$_name() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method invoked on uninitialized component of type 'PackageAccessRepositoryExample'" );
    }
    return "PackageAccessRepositoryExample." + getId();
  }

  private boolean $$arezi$$_observe() {
    final boolean isDisposed = isDisposed();
    if ( !isDisposed )  {
      this.$$arezi$$_disposedObservable.reportObserved();
    }
    return !isDisposed;
  }

  @Override
  public boolean observe() {
    return $$arezi$$_observe();
  }

  @Override
  public boolean isDisposed() {
    return ComponentState.isDisposingOrDisposed( this.$$arezi$$_state );
  }

  @Override
  public void dispose() {
    if ( !ComponentState.isDisposingOrDisposed( this.$$arezi$$_state ) ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSING;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arezi$$_component.dispose();
      } else {
        $$arezi$$_context().dispose( Arez.areNamesEnabled() ? $$arezi$$_name() : null, () -> { {
          this.$$arezi$$_disposedObservable.dispose();
          this.$$arez$$_qualifiedName.dispose();
          this.$$arez$$_name.dispose();
          this.$$arez$$_packageName.dispose();
          this.$$arez$$_rawQualifiedName.dispose();
        } } );
      }
      this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSED;
    }
  }

  @Nonnull
  @Override
  public String getName() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_name.reportObserved();
    return super.getName();
  }

  @Override
  public void setName(@Nonnull final String name) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( !Objects.equals( name, super.getName() ) ) {
      this.$$arez$$_name.preReportChanged();
      super.setName(name);
      this.$$arez$$_name.reportChanged();
    }
  }

  @Nonnull
  @Override
  public String getPackageName() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_packageName.reportObserved();
    return super.getPackageName();
  }

  @Override
  public void setPackageName(@Nonnull final String packageName) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( !Objects.equals( packageName, super.getPackageName() ) ) {
      this.$$arez$$_packageName.preReportChanged();
      super.setPackageName(packageName);
      this.$$arez$$_packageName.reportChanged();
    }
  }

  @Nullable
  @Override
  public String getRawQualifiedName() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_rawQualifiedName.reportObserved();
    return super.getRawQualifiedName();
  }

  @Override
  public void setQualifiedName(@Nullable final String qualifiedName) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( !Objects.equals( qualifiedName, super.getRawQualifiedName() ) ) {
      this.$$arez$$_rawQualifiedName.preReportChanged();
      super.setQualifiedName(qualifiedName);
      this.$$arez$$_rawQualifiedName.reportChanged();
    }
  }

  @Nonnull
  @Override
  public String getQualifiedName() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    return this.$$arez$$_qualifiedName.get();
  }

  @Override
  public final int hashCode() {
    return Integer.hashCode( getId() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_PackageAccessRepositoryExample) ) {
      return false;
    } else {
      final Arez_PackageAccessRepositoryExample that = (Arez_PackageAccessRepositoryExample) o;;
      return getId() == that.getId();
    }
  }

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + $$arezi$$_name() + "]";
    } else {
      return super.toString();
    }
  }
}
