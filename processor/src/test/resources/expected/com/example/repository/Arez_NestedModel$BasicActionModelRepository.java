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
public final class Arez_NestedModel$BasicActionModelRepository extends NestedModel$BasicActionModelRepository implements Disposable {
  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_entities;

  Arez_NestedModel$BasicActionModelRepository() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_entities = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? "NestedModel$BasicActionModelRepository.entities" : null );
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
      $$arez$$_entities.dispose();
    }
  }

  @Nonnull
  @Override
  protected Collection<NestedModel.BasicActionModel> entities() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'NestedModel$BasicActionModelRepository'" );
    this.$$arez$$_entities.reportObserved();
    return super.entities();
  }

  @Override
  Observable getEntitiesObservable() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'NestedModel$BasicActionModelRepository'" );
    return $$arez$$_entities;
  }

  @Nonnull
  @Override
  public NestedModel.BasicActionModel create() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'NestedModel$BasicActionModelRepository'" );
    try {
      return this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? "NestedModel$BasicActionModelRepository.create" : null, true, () -> super.create() );
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
  public void destroy(@Nonnull final NestedModel.BasicActionModel entity) {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'NestedModel$BasicActionModelRepository'" );
    try {
      this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? "NestedModel$BasicActionModelRepository.destroy" : null, true, () -> super.destroy(entity), entity );
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
    if ( $$arez$$_context.areNamesEnabled() ) {
      return "ArezComponent[NestedModel$BasicActionModelRepository]";
    } else {
      return super.toString();
    }
  }
}
