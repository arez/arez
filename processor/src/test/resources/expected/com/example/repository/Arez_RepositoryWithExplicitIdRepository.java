package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.spy.ActionCompletedEvent;
import org.realityforge.arez.spy.ActionStartedEvent;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_RepositoryWithExplicitIdRepository extends RepositoryWithExplicitIdRepository implements Disposable {
  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  Arez_RepositoryWithExplicitIdRepository() {
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
  public RepositoryWithExplicitId create(@Nonnull final String packageName, @Nonnull final String name) {
    assert !$$arez$$_disposed;
    Throwable $$arez$$_throwable = null;
    boolean $$arez$$_completed = false;
    long $$arez$$_startedAt = 0L;
    try {
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        $$arez$$_startedAt = System.currentTimeMillis();
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionStartedEvent( "RepositoryWithExplicitIdRepository.create_packageName_name", false, new Object[]{packageName,name} ) );
      }
      final RepositoryWithExplicitId $$arez$$_result = this.$$arez$$_context.safeFunction(this.$$arez$$_context.areNamesEnabled() ? "RepositoryWithExplicitIdRepository.create_packageName_name" : null, true, () -> super.create(packageName,name) );
      $$arez$$_completed = true;
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( "RepositoryWithExplicitIdRepository.create_packageName_name", false, new Object[]{packageName,name}, true, $$arez$$_result, $$arez$$_throwable, $$arez$$_duration ) );
      }
      return $$arez$$_result;
    } catch( final RuntimeException $$arez$$_e ) {
      $$arez$$_throwable = $$arez$$_e;
      throw $$arez$$_e;
    } catch( final Exception $$arez$$_e ) {
      $$arez$$_throwable = $$arez$$_e;
      throw new IllegalStateException( $$arez$$_e );
    } catch( final Error $$arez$$_e ) {
      $$arez$$_throwable = $$arez$$_e;
      throw $$arez$$_e;
    } catch( final Throwable $$arez$$_e ) {
      $$arez$$_throwable = $$arez$$_e;
      throw new IllegalStateException( $$arez$$_e );
    } finally {
      if ( !$$arez$$_completed ) {
        final RepositoryWithExplicitId $$arez$$_result = null;
        if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
          final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
          this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( "RepositoryWithExplicitIdRepository.create_packageName_name", false, new Object[]{packageName,name}, true, $$arez$$_result, $$arez$$_throwable, $$arez$$_duration ) );
        }
      }
    }
  }
}
