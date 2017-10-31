package com.example.repository;

import java.util.Collection;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.braincheck.Guards;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_RepositoryWithImplicitIdRepository extends RepositoryWithImplicitIdRepository implements Disposable {
  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_entities;

  Arez_RepositoryWithImplicitIdRepository() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_entities = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? "RepositoryWithImplicitIdRepository.entities" : null );
  }

  @Override
  public boolean isDisposed() {
    return this.$$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      this.$$arez$$_disposed = true;
      this.$$arez$$_context.safeAction( Arez.areNamesEnabled() ? "RepositoryWithImplicitIdRepository.dispose" : null, () -> { {
        super.preDispose();
        this.$$arez$$_entities.dispose();
      } } );
    }
  }

  @Nonnull
  @Override
  protected Collection<RepositoryWithImplicitId> entities() {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component 'RepositoryWithImplicitIdRepository'" );
    this.$$arez$$_entities.reportObserved();
    return super.entities();
  }

  @Override
  Observable getEntitiesObservable() {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component 'RepositoryWithImplicitIdRepository'" );
    return $$arez$$_entities;
  }

  @Override
  public void destroy(@Nonnull final RepositoryWithImplicitId entity) {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component 'RepositoryWithImplicitIdRepository'" );
    try {
      this.$$arez$$_context.safeAction(Arez.areNamesEnabled() ? "RepositoryWithImplicitIdRepository.destroy" : null, true, () -> super.destroy(entity), entity );
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
  RepositoryWithImplicitId create(@Nonnull final String packageName, @Nonnull final String name) {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component 'RepositoryWithImplicitIdRepository'" );
    try {
      return this.$$arez$$_context.safeAction(Arez.areNamesEnabled() ? "RepositoryWithImplicitIdRepository.create_packageName_name" : null, true, () -> super.create(packageName,name), packageName, name );
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

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[RepositoryWithImplicitIdRepository]";
    } else {
      return super.toString();
    }
  }
}
