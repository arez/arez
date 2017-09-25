package com.example.container_name;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.spy.ActionCompletedEvent;
import org.realityforge.arez.spy.ActionStartedEvent;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_ContainerNamePrefixOnSingletonModel extends ContainerNamePrefixOnSingletonModel implements Disposable {
  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  public Arez_ContainerNamePrefixOnSingletonModel() {
    super();
    this.$$arez$$_context = Arez.context();
  }

  @Nonnull
  public final String getTypeName() {
    return "ContainerNamePrefixOnSingletonModel";
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
    }
  }

  @Override
  public void doStuff(final long time, final float someOtherParameter) {
    assert !$$arez$$_disposed;
    Throwable $$arez$$_throwable = null;
    boolean $$arez$$_completed = false;
    long $$arez$$_startedAt = 0L;
    try {
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        $$arez$$_startedAt = System.currentTimeMillis();
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionStartedEvent( "ContainerNamePrefixOnSingletonModel.doStuff", new Object[]{time,someOtherParameter} ) );
      }
      this.$$arez$$_context.safeProcedure(this.$$arez$$_context.areNamesEnabled() ? "ContainerNamePrefixOnSingletonModel.doStuff" : null, true, () -> super.doStuff(time,someOtherParameter) );
      $$arez$$_completed = true;
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( "ContainerNamePrefixOnSingletonModel.doStuff", new Object[]{time,someOtherParameter}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
      }
    } catch( final RuntimeException e ) {
      $$arez$$_throwable = e;
      throw e;
    } catch( final Exception e ) {
      $$arez$$_throwable = e;
      throw new IllegalStateException( e );
    } catch( final Error e ) {
      $$arez$$_throwable = e;
      throw e;
    } catch( final Throwable e ) {
      $$arez$$_throwable = e;
      throw new IllegalStateException( e );
    } finally {
      if ( !$$arez$$_completed ) {
        if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
          final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
          this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( "ContainerNamePrefixOnSingletonModel.doStuff", new Object[]{time,someOtherParameter}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
        }
      }
    }
  }
}
