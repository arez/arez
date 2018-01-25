package com.example.repository;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputedValue;
import arez.Disposable;
import arez.Observable;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
final class Arez_CompleteRepositoryExample extends CompleteRepositoryExample implements Disposable {
  private boolean $$arez$$_disposed;

  private OnDispose $$arez$$_onDispose;

  @Nullable
  private final ArezContext $$arez$$_context;

  private final Component $$arez$$_component;

  private final Observable<Boolean> $$arez$$_disposedObservable;

  @Nonnull
  private final Observable<String> $$arez$$_name;

  @Nonnull
  private final Observable<String> $$arez$$_packageName;

  @Nonnull
  private final Observable<String> $$arez$$_rawQualifiedName;

  @Nonnull
  private final ComputedValue<String> $$arez$$_qualifiedName;

  Arez_CompleteRepositoryExample(@Nonnull final String packageName, @Nonnull final String name) {
    super(packageName,name);
    this.$$arez$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arez$$_component = Arez.areNativeComponentsEnabled() ? $$arez$$_context().createComponent( "CompleteRepositoryExample", getId(), $$arez$$_name(), () -> $$arez$$_preDispose(), null ) : null;
    this.$$arez$$_disposedObservable = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arez$$_disposed : null, null );
    this.$$arez$$_name = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".name" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getName() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setName( v ) : null );
    this.$$arez$$_packageName = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".packageName" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getPackageName() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setPackageName( v ) : null );
    this.$$arez$$_rawQualifiedName = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".rawQualifiedName" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getRawQualifiedName() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setQualifiedName( v ) : null );
    this.$$arez$$_qualifiedName = $$arez$$_context().createComputedValue( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".qualifiedName" : null, super::getQualifiedName, Objects::equals, null, null, null, null );
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arez$$_component.complete();
    }
  }

  final ArezContext $$arez$$_context() {
    return Arez.areZonesEnabled() ? this.$$arez$$_context : Arez.context();
  }

  String $$arez$$_name() {
    return "CompleteRepositoryExample." + getId();
  }

  @Override
  public boolean isDisposed() {
    if ( $$arez$$_context().isTransactionActive() && !this.$$arez$$_disposedObservable.isDisposed() )  {
      this.$$arez$$_disposedObservable.reportObserved();
      return this.$$arez$$_disposed;
    } else {
      return this.$$arez$$_disposed;
    }
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      this.$$arez$$_disposed = true;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arez$$_component.dispose();
      } else {
        $$arez$$_context().safeAction( Arez.areNamesEnabled() ? $$arez$$_name() + ".dispose" : null, () -> { {
          $$arez$$_preDispose();
          this.$$arez$$_disposedObservable.dispose();
          this.$$arez$$_qualifiedName.dispose();
          this.$$arez$$_name.dispose();
          this.$$arez$$_packageName.dispose();
          this.$$arez$$_rawQualifiedName.dispose();
        } } );
      }
    }
  }

  void $$arez$$_preDispose() {
    if ( null != this.$$arez$$_onDispose ) {
      this.$$arez$$_onDispose.onDispose( this );
      this.$$arez$$_onDispose = null;
    }
  }

  void $$arez$$_setOnDispose(OnDispose onDispose) {
    this.$$arez$$_onDispose = onDispose;
  }

  @Nonnull
  @Override
  public String getName() {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    this.$$arez$$_name.reportObserved();
    return super.getName();
  }

  @Override
  public void setName(@Nonnull final String name) {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    if ( !Objects.equals( name, super.getName() ) ) {
      this.$$arez$$_name.preReportChanged();
      super.setName(name);
      this.$$arez$$_name.reportChanged();
    }
  }

  @Nonnull
  @Override
  public String getPackageName() {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    this.$$arez$$_packageName.reportObserved();
    return super.getPackageName();
  }

  @Override
  public void setPackageName(@Nonnull final String packageName) {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    if ( !Objects.equals( packageName, super.getPackageName() ) ) {
      this.$$arez$$_packageName.preReportChanged();
      super.setPackageName(packageName);
      this.$$arez$$_packageName.reportChanged();
    }
  }

  @Nullable
  @Override
  public String getRawQualifiedName() {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    this.$$arez$$_rawQualifiedName.reportObserved();
    return super.getRawQualifiedName();
  }

  @Override
  public void setQualifiedName(@Nullable final String qualifiedName) {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    if ( !Objects.equals( qualifiedName, super.getRawQualifiedName() ) ) {
      this.$$arez$$_rawQualifiedName.preReportChanged();
      super.setQualifiedName(qualifiedName);
      this.$$arez$$_rawQualifiedName.reportChanged();
    }
  }

  @Nonnull
  @Override
  public String getQualifiedName() {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
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
    } else if ( null == o || !(o instanceof Arez_CompleteRepositoryExample) ) {
      return false;
    } else {
      final Arez_CompleteRepositoryExample that = (Arez_CompleteRepositoryExample) o;;
      return getId() == that.getId();
    }
  }

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + $$arez$$_name() + "]";
    } else {
      return super.toString();
    }
  }

  @FunctionalInterface
  interface OnDispose {
    void onDispose(Arez_CompleteRepositoryExample entity);
  }
}
