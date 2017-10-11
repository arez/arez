package com.example.tracked;

import java.text.ParseException;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observer;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_TrackedAllTypesModel extends TrackedAllTypesModel implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observer $$arez$$_render2;

  @Nonnull
  private final Observer $$arez$$_render3;

  @Nonnull
  private final Observer $$arez$$_render4;

  @Nonnull
  private final Observer $$arez$$_render1;

  public Arez_TrackedAllTypesModel() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_render2 = this.$$arez$$_context.tracker( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".render2" : null, true, super::onRender2DepsUpdated );
    this.$$arez$$_render3 = this.$$arez$$_context.tracker( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".render3" : null, true, super::onRender3DepsUpdated );
    this.$$arez$$_render4 = this.$$arez$$_context.tracker( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".render4" : null, true, super::onRender4DepsUpdated );
    this.$$arez$$_render1 = this.$$arez$$_context.tracker( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".render1" : null, true, super::onRender1DepsUpdated );
  }

  final long $$arez$$_id() {
    return $$arez$$_id;
  }

  String $$arez$$_name() {
    return "TrackedAllTypesModel." + $$arez$$_id();
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      $$arez$$_render2.dispose();
      $$arez$$_render3.dispose();
      $$arez$$_render4.dispose();
      $$arez$$_render1.dispose();
    }
  }

  @Override
  public void render2() throws ParseException {
    assert !$$arez$$_disposed;
    assert !$$arez$$_disposed;
    try {
      this.$$arez$$_context.track( this.$$arez$$_render2, () -> super.render2() );
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
  protected int render3() {
    assert !$$arez$$_disposed;
    assert !$$arez$$_disposed;
    try {
      return this.$$arez$$_context.safeTrack( this.$$arez$$_render3, () -> super.render3() );
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
  int render4() throws ParseException {
    assert !$$arez$$_disposed;
    assert !$$arez$$_disposed;
    try {
      return this.$$arez$$_context.track( this.$$arez$$_render4, () -> super.render4() );
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
  public void render1() {
    assert !$$arez$$_disposed;
    assert !$$arez$$_disposed;
    try {
      this.$$arez$$_context.safeTrack( this.$$arez$$_render1, () -> super.render1() );
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
    } else if ( null == o || !(o instanceof Arez_TrackedAllTypesModel) ) {
      return false;
    } else {
      final Arez_TrackedAllTypesModel that = (Arez_TrackedAllTypesModel) o;;
      return $$arez$$_id() == that.$$arez$$_id();
    }
  }
}
