package com.example.tracked;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Observable;
import arez.Observer;
import java.text.ParseException;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public final class Arez_TrackedAllTypesModel extends TrackedAllTypesModel implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  @Nullable
  private final ArezContext $$arez$$_context;

  private final Component $$arez$$_component;

  private final Observable<Boolean> $$arez$$_disposedObservable;

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
    this.$$arez$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_component = Arez.areNativeComponentsEnabled() ? $$arez$$_context().createComponent( "TrackedAllTypesModel", $$arez$$_id(), $$arez$$_name(), null, null ) : null;
    this.$$arez$$_disposedObservable = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arez$$_disposed : null, null );
    this.$$arez$$_render2 = $$arez$$_context().tracker( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".render2" : null, true, () -> super.onRender2DepsChanged() );
    this.$$arez$$_render3 = $$arez$$_context().tracker( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".render3" : null, true, () -> super.onRender3DepsChanged() );
    this.$$arez$$_render4 = $$arez$$_context().tracker( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".render4" : null, true, () -> super.onRender4DepsChanged() );
    this.$$arez$$_render1 = $$arez$$_context().tracker( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".render1" : null, true, () -> super.onRender1DepsChanged() );
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arez$$_component.complete();
    }
  }

  final ArezContext $$arez$$_context() {
    return Arez.areZonesEnabled() ? this.$$arez$$_context : Arez.context();
  }

  final long $$arez$$_id() {
    return this.$$arez$$_id;
  }

  String $$arez$$_name() {
    return "TrackedAllTypesModel." + $$arez$$_id();
  }

  @Override
  public boolean isDisposed() {
    if ( $$arez$$_context().isTransactionActive() && !this.$$arez$$_disposedObservable.isDisposed() )  {
      this.$$arez$$_disposedObservable.reportObserved();
      return this.$$arez$$_disposed;
    } else {
      return this.$$arez$$_disposed;
    }
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      this.$$arez$$_disposed = true;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arez$$_component.dispose();
      } else {
        $$arez$$_context().safeAction( Arez.areNamesEnabled() ? $$arez$$_name() + ".dispose" : null, () -> { {
          this.$$arez$$_disposedObservable.dispose();
          this.$$arez$$_render2.dispose();
          this.$$arez$$_render3.dispose();
          this.$$arez$$_render4.dispose();
          this.$$arez$$_render1.dispose();
        } } );
      }
    }
  }

  @Override
  public void render2() throws ParseException {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    try {
      $$arez$$_context().track( this.$$arez$$_render2, () -> super.render2() );
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
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    try {
      return $$arez$$_context().safeTrack( this.$$arez$$_render3, () -> super.render3() );
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
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    try {
      return $$arez$$_context().track( this.$$arez$$_render4, () -> super.render4() );
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
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    try {
      $$arez$$_context().safeTrack( this.$$arez$$_render1, () -> super.render1() );
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

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + $$arez$$_name() + "]";
    } else {
      return super.toString();
    }
  }
}
