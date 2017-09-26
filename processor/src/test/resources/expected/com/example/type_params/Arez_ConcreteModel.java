package com.example.type_params;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.spy.ActionCompletedEvent;
import org.realityforge.arez.spy.ActionStartedEvent;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
public final class Arez_ConcreteModel<W extends Writer> extends ConcreteModel<W> implements Disposable {
  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final ComputedValue<IOException> $$arez$$_compError;

  @Nonnull
  private final ComputedValue<W> $$arez$$_compWriter;

  public Arez_ConcreteModel(final W writer) {
    super(writer);
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_compError = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + ".compError" : null, super::compError, Objects::equals, null, null, null, null );
    this.$$arez$$_compWriter = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + ".compWriter" : null, super::compWriter, Objects::equals, null, null, null, null );
  }

  public Arez_ConcreteModel(final IOException error) {
    super(error);
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_compError = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + ".compError" : null, super::compError, Objects::equals, null, null, null, null );
    this.$$arez$$_compWriter = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + ".compWriter" : null, super::compWriter, Objects::equals, null, null, null, null );
  }

  public Arez_ConcreteModel(final IOException error, final W writer, final int i) {
    super(error,writer,i);
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_compError = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + ".compError" : null, super::compError, Objects::equals, null, null, null, null );
    this.$$arez$$_compWriter = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + ".compWriter" : null, super::compWriter, Objects::equals, null, null, null, null );
  }

  private String $$arez$$_id() {
    return "ConcreteModel." + getContainerId();
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      $$arez$$_compError.dispose();
      $$arez$$_compWriter.dispose();
    }
  }

  @Override
  public void handleWriter(final W writer) {
    assert !$$arez$$_disposed;
    Throwable $$arez$$_throwable = null;
    boolean $$arez$$_completed = false;
    long $$arez$$_startedAt = 0L;
    try {
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        $$arez$$_startedAt = System.currentTimeMillis();
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionStartedEvent( $$arez$$_id() + ".handleWriter", false, new Object[]{writer} ) );
      }
      this.$$arez$$_context.safeProcedure(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + ".handleWriter" : null, true, () -> super.handleWriter(writer) );
      $$arez$$_completed = true;
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_id() + ".handleWriter", false, new Object[]{writer}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
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
          this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_id() + ".handleWriter", false, new Object[]{writer}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
        }
      }
    }
  }

  @Override
  public void handleError(final IOException error) {
    assert !$$arez$$_disposed;
    Throwable $$arez$$_throwable = null;
    boolean $$arez$$_completed = false;
    long $$arez$$_startedAt = 0L;
    try {
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        $$arez$$_startedAt = System.currentTimeMillis();
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionStartedEvent( $$arez$$_id() + ".handleError", false, new Object[]{error} ) );
      }
      this.$$arez$$_context.safeProcedure(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + ".handleError" : null, true, () -> super.handleError(error) );
      $$arez$$_completed = true;
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_id() + ".handleError", false, new Object[]{error}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
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
          this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_id() + ".handleError", false, new Object[]{error}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
        }
      }
    }
  }

  @Override
  public W genWriter() {
    assert !$$arez$$_disposed;
    Throwable $$arez$$_throwable = null;
    boolean $$arez$$_completed = false;
    long $$arez$$_startedAt = 0L;
    try {
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        $$arez$$_startedAt = System.currentTimeMillis();
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionStartedEvent( $$arez$$_id() + ".genWriter", false, new Object[]{} ) );
      }
      final W $$arez$$_result = this.$$arez$$_context.safeFunction(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + ".genWriter" : null, true, () -> super.genWriter() );
      $$arez$$_completed = true;
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_id() + ".genWriter", false, new Object[]{}, true, $$arez$$_result, $$arez$$_throwable, $$arez$$_duration ) );
      }
      return $$arez$$_result;
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
        final W $$arez$$_result = null;
        if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
          final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
          this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_id() + ".genWriter", false, new Object[]{}, true, $$arez$$_result, $$arez$$_throwable, $$arez$$_duration ) );
        }
      }
    }
  }

  @Override
  public IOException genError() {
    assert !$$arez$$_disposed;
    Throwable $$arez$$_throwable = null;
    boolean $$arez$$_completed = false;
    long $$arez$$_startedAt = 0L;
    try {
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        $$arez$$_startedAt = System.currentTimeMillis();
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionStartedEvent( $$arez$$_id() + ".genError", false, new Object[]{} ) );
      }
      final IOException $$arez$$_result = this.$$arez$$_context.safeFunction(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + ".genError" : null, true, () -> super.genError() );
      $$arez$$_completed = true;
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_id() + ".genError", false, new Object[]{}, true, $$arez$$_result, $$arez$$_throwable, $$arez$$_duration ) );
      }
      return $$arez$$_result;
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
        final IOException $$arez$$_result = null;
        if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
          final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
          this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_id() + ".genError", false, new Object[]{}, true, $$arez$$_result, $$arez$$_throwable, $$arez$$_duration ) );
        }
      }
    }
  }

  @Override
  public IOException compError() {
    return this.$$arez$$_compError.get();
  }

  @Override
  public W compWriter() {
    return this.$$arez$$_compWriter.get();
  }
}
