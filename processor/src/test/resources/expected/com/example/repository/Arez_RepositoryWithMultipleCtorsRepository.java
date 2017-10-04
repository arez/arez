package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_RepositoryWithMultipleCtorsRepository extends RepositoryWithMultipleCtorsRepository implements Disposable {
  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  Arez_RepositoryWithMultipleCtorsRepository() {
    super();
    this.$$arez$$_context = Arez.context();
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      super.preDispose();
    }
  }

  @Nonnull
  @Override
  public RepositoryWithMultipleCtors create(@Nonnull final String packageName, @Nonnull final String name) {
    assert !$$arez$$_disposed;
    try {
      return this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? "RepositoryWithMultipleCtorsRepository.create_packageName_name" : null, true, () -> super.create(packageName,name), packageName, name );
    } catch( final RuntimeException $$arez$$_e ) {
      throw $$arez$$_e;
    } catch( final Exception $$arez$$_e ) {
      throw new IllegalStateException( $$arez$$_e );
    } catch( final Error $$arez$$_e ) {
      throw $$arez$$_e;
    } catch( final Throwable $$arez$$_e ) {
      throw new IllegalStateException( $$arez$$_e );
    }
  }

  @Nonnull
  @Override
  public RepositoryWithMultipleCtors create() {
    assert !$$arez$$_disposed;
    try {
      return this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? "RepositoryWithMultipleCtorsRepository.create_" : null, true, () -> super.create() );
    } catch( final RuntimeException $$arez$$_e ) {
      throw $$arez$$_e;
    } catch( final Exception $$arez$$_e ) {
      throw new IllegalStateException( $$arez$$_e );
    } catch( final Error $$arez$$_e ) {
      throw $$arez$$_e;
    } catch( final Throwable $$arez$$_e ) {
      throw new IllegalStateException( $$arez$$_e );
    }
  }

  @Nonnull
  @Override
  public RepositoryWithMultipleCtors create(@Nonnull final String name) {
    assert !$$arez$$_disposed;
    try {
      return this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? "RepositoryWithMultipleCtorsRepository.create_name" : null, true, () -> super.create(name), name );
    } catch( final RuntimeException $$arez$$_e ) {
      throw $$arez$$_e;
    } catch( final Exception $$arez$$_e ) {
      throw new IllegalStateException( $$arez$$_e );
    } catch( final Error $$arez$$_e ) {
      throw $$arez$$_e;
    } catch( final Throwable $$arez$$_e ) {
      throw new IllegalStateException( $$arez$$_e );
    }
  }
}
