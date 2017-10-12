package com.example.tracked;

import java.text.ParseException;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observer;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_BasicTrackedWithExceptionsModel extends BasicTrackedWithExceptionsModel implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observer $$arez$$_render;

  public Arez_BasicTrackedWithExceptionsModel() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_render = this.$$arez$$_context.tracker( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".render" : null, true, super::onRenderDepsUpdated );
  }

  final long $$arez$$_id() {
    return $$arez$$_id;
  }

  String $$arez$$_name() {
    return "BasicTrackedWithExceptionsModel." + $$arez$$_id();
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      $$arez$$_render.dispose();
    }
  }

  @Override
  public void render() throws ParseException {
    assert !$$arez$$_disposed;
    assert !$$arez$$_disposed;
    try {
      this.$$arez$$_context.track( this.$$arez$$_render, () -> super.render() );
    } catch( final ParseException $$arez$$_e ) {
      throw $$arez$$_e;
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
  public final int hashCode() {
    return Long.hashCode( $$arez$$_id() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_BasicTrackedWithExceptionsModel) ) {
      return false;
    } else {
      final Arez_BasicTrackedWithExceptionsModel that = (Arez_BasicTrackedWithExceptionsModel) o;;
      return $$arez$$_id() == that.$$arez$$_id();
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
