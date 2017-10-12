package com.example.repository;

import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
public final class Arez_CompleteRepositoryExample extends CompleteRepositoryExample implements Disposable {
  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_name;

  @Nonnull
  private final Observable $$arez$$_packageName;

  @Nonnull
  private final Observable $$arez$$_rawQualifiedName;

  @Nonnull
  private final ComputedValue<String> $$arez$$_qualifiedName;

  Arez_CompleteRepositoryExample(@Nonnull final String packageName, @Nonnull final String name) {
    super(packageName,name);
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_name = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".name" : null );
    this.$$arez$$_packageName = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".packageName" : null );
    this.$$arez$$_rawQualifiedName = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".rawQualifiedName" : null );
    this.$$arez$$_qualifiedName = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".qualifiedName" : null, super::getQualifiedName, Objects::equals, null, null, null, null );
  }

  String $$arez$$_name() {
    return "CompleteRepositoryExample." + getId();
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      $$arez$$_qualifiedName.dispose();
      $$arez$$_name.dispose();
      $$arez$$_packageName.dispose();
      $$arez$$_rawQualifiedName.dispose();
    }
  }

  @Nonnull
  @Override
  public String getName() {
    assert !$$arez$$_disposed;
    this.$$arez$$_name.reportObserved();
    return super.getName();
  }

  @Override
  public void setName(@Nonnull final String name) {
    assert !$$arez$$_disposed;
    if ( !Objects.equals(name, super.getName()) ) {
      super.setName(name);
      this.$$arez$$_name.reportChanged();
    }
  }

  @Nonnull
  @Override
  public String getPackageName() {
    assert !$$arez$$_disposed;
    this.$$arez$$_packageName.reportObserved();
    return super.getPackageName();
  }

  @Override
  public void setPackageName(@Nonnull final String packageName) {
    assert !$$arez$$_disposed;
    if ( !Objects.equals(packageName, super.getPackageName()) ) {
      super.setPackageName(packageName);
      this.$$arez$$_packageName.reportChanged();
    }
  }

  @Nullable
  @Override
  public String getRawQualifiedName() {
    assert !$$arez$$_disposed;
    this.$$arez$$_rawQualifiedName.reportObserved();
    return super.getRawQualifiedName();
  }

  @Override
  public void setQualifiedName(@Nullable final String qualifiedName) {
    assert !$$arez$$_disposed;
    if ( !Objects.equals(qualifiedName, super.getRawQualifiedName()) ) {
      super.setQualifiedName(qualifiedName);
      this.$$arez$$_rawQualifiedName.reportChanged();
    }
  }

  @Nonnull
  @Override
  public String getQualifiedName() {
    assert !$$arez$$_disposed;
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
    if ( $$arez$$_context.areNamesEnabled() ) {
      return "ArezComponent[" + $$arez$$_name() + "]";
    } else {
      return super.toString();
    }
  }
}
