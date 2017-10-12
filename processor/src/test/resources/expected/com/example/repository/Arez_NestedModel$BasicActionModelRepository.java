package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_NestedModel$BasicActionModelRepository extends NestedModel$BasicActionModelRepository implements Disposable {
  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  Arez_NestedModel$BasicActionModelRepository() {
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
  public NestedModel.BasicActionModel create() {
    assert !$$arez$$_disposed;
    try {
      return this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? "NestedModel$BasicActionModelRepository.create_" : null, true, () -> super.create() );
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
