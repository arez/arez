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
    this.$$arez$$_compError = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".compError" : null, super::compError, Objects::equals, null, null, null, null );
    this.$$arez$$_compWriter = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".compWriter" : null, super::compWriter, Objects::equals, null, null, null, null );
  }

  public Arez_ConcreteModel(final IOException error) {
    super(error);
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_compError = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".compError" : null, super::compError, Objects::equals, null, null, null, null );
    this.$$arez$$_compWriter = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".compWriter" : null, super::compWriter, Objects::equals, null, null, null, null );
  }

  public Arez_ConcreteModel(final IOException error, final W writer, final int i) {
    super(error,writer,i);
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_compError = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".compError" : null, super::compError, Objects::equals, null, null, null, null );
    this.$$arez$$_compWriter = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".compWriter" : null, super::compWriter, Objects::equals, null, null, null, null );
  }

  String $$arez$$_name() {
    return "ConcreteModel." + getComponentId();
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
    try {
      this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".handleWriter" : null, true, () -> super.handleWriter(writer), writer );
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
  public void handleError(final IOException error) {
    assert !$$arez$$_disposed;
    try {
      this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".handleError" : null, true, () -> super.handleError(error), error );
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
  public W genWriter() {
    assert !$$arez$$_disposed;
    try {
      return this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".genWriter" : null, true, () -> super.genWriter() );
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
  public IOException genError() {
    assert !$$arez$$_disposed;
    try {
      return this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".genError" : null, true, () -> super.genError() );
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
  public IOException compError() {
    assert !$$arez$$_disposed;
    return this.$$arez$$_compError.get();
  }

  @Override
  public W compWriter() {
    assert !$$arez$$_disposed;
    return this.$$arez$$_compWriter.get();
  }

  @Override
  public final int hashCode() {
    return null != getComponentId() ? getComponentId().hashCode() : System.identityHashCode( this );
  }
}
