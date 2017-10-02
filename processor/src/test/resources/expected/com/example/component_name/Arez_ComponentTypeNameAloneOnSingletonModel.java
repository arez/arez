package com.example.component_name;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.spy.ActionCompletedEvent;
import org.realityforge.arez.spy.ActionStartedEvent;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_ComponentTypeNameAloneOnSingletonModel extends ComponentTypeNameAloneOnSingletonModel implements Disposable {
  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  public Arez_ComponentTypeNameAloneOnSingletonModel() {
    super();
    this.$$arez$$_context = Arez.context();
  }

  @Nonnull
  public final String getTypeName() {
    return "ComponentTypeNameAloneOnSingletonModel";
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
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionStartedEvent( "ComponentTypeNameAloneOnSingletonModel.doStuff", false, new Object[]{time,someOtherParameter} ) );
      }
      this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? "ComponentTypeNameAloneOnSingletonModel.doStuff" : null, true, () -> super.doStuff(time,someOtherParameter) );
      $$arez$$_completed = true;
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( "ComponentTypeNameAloneOnSingletonModel.doStuff", false, new Object[]{time,someOtherParameter}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
      }
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
        if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
          final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
          this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( "ComponentTypeNameAloneOnSingletonModel.doStuff", false, new Object[]{time,someOtherParameter}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
        }
      }
    }
  }
}
