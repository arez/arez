import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputedValue;
import arez.Disposable;
import arez.Observable;
import arez.Observer;
import arez.component.ComponentObservable;
import arez.component.ComponentState;
import arez.component.Identifiable;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
@Singleton
public final class Arez_ImplicitSingletonModel extends ImplicitSingletonModel implements Disposable, Identifiable<Long>, ComponentObservable {
  private static volatile long $$arezi$$_nextId;

  private final long $$arezi$$_id;

  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final Observable<Boolean> $$arezi$$_disposedObservable;

  @Nonnull
  private final Observable<Long> $$arez$$_time;

  @Nonnull
  private final ComputedValue<Integer> $$arez$$_someValue;

  @Nonnull
  private final Observer $$arez$$_myAutorun;

  @Nonnull
  private final Observer $$arez$$_render;

  @Inject
  public Arez_ImplicitSingletonModel() {
    super();
    this.$$arezi$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arezi$$_id = $$arezi$$_nextId++;
    this.$$arezi$$_state = ComponentState.COMPONENT_INITIALIZED;
    this.$$arezi$$_component = Arez.areNativeComponentsEnabled() ? $$arezi$$_context().createComponent( "ImplicitSingletonModel", $$arezi$$_id(), $$arezi$$_name(), null, null ) : null;
    this.$$arezi$$_disposedObservable = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezi$$_state >= 0 : null, null );
    this.$$arez$$_time = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".time" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getTime() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setTime( v ) : null );
    this.$$arez$$_someValue = $$arezi$$_context().createComputedValue( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".someValue" : null, super::someValue, Objects::equals, null, null, null, null );
    this.$$arez$$_myAutorun = $$arezi$$_context().autorun( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".myAutorun" : null, false, () -> super.myAutorun(), false );
    this.$$arez$$_render = $$arezi$$_context().tracker( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".render" : null, false, () -> super.onRenderDepsChanged() );
    this.$$arezi$$_state = ComponentState.COMPONENT_CONSTRUCTED;
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arezi$$_component.complete();
    }
    this.$$arezi$$_state = ComponentState.COMPONENT_COMPLETE;
    $$arezi$$_context().triggerScheduler();
    this.$$arezi$$_state = ComponentState.COMPONENT_READY;
  }

  final ArezContext $$arezi$$_context() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method invoked on uninitialized component of type 'ImplicitSingletonModel'" );
    }
    return Arez.areZonesEnabled() ? this.$$arezi$$_context : Arez.context();
  }

  final long $$arezi$$_id() {
    return this.$$arezi$$_id;
  }

  @Override
  @Nonnull
  public final Long getArezId() {
    return $$arezi$$_id();
  }

  String $$arezi$$_name() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method invoked on uninitialized component of type 'ImplicitSingletonModel'" );
    }
    return "ImplicitSingletonModel";
  }

  @Override
  public boolean observe() {
    final boolean isDisposed = ComponentState.isDisposingOrDisposed( this.$$arezi$$_state );
    if ( !isDisposed )  {
      this.$$arezi$$_disposedObservable.reportObserved();
    }
    return !isDisposed;
  }

  @Override
  public boolean isDisposed() {
    return ComponentState.isDisposingOrDisposed( this.$$arezi$$_state );
  }

  @Override
  public void dispose() {
    if ( !ComponentState.isDisposingOrDisposed( this.$$arezi$$_state ) ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSING;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arezi$$_component.dispose();
      } else {
        $$arezi$$_context().dispose( Arez.areNamesEnabled() ? $$arezi$$_name() : null, () -> { {
          this.$$arezi$$_disposedObservable.dispose();
          this.$$arez$$_myAutorun.dispose();
          this.$$arez$$_render.dispose();
          this.$$arez$$_someValue.dispose();
          this.$$arez$$_time.dispose();
        } } );
      }
      this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSED;
    }
  }

  @Override
  public long getTime() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime(final long time) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( time != super.getTime() ) {
      this.$$arez$$_time.preReportChanged();
      super.setTime(time);
      this.$$arez$$_time.reportChanged();
    }
  }

  @Override
  public void myAutorun() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    $$arezi$$_context().safeAction( Arez.areNamesEnabled() ? $$arezi$$_name() + ".myAutorun" : null, false, () -> super.myAutorun() );
  }

  @Override
  public void doStuff(final long time) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    try {
      $$arezi$$_context().safeAction(Arez.areNamesEnabled() ? $$arezi$$_name() + ".doStuff" : null, true, () -> super.doStuff(time), time );
    } catch( final RuntimeException | Error $$arez_exception$$ ) {
      throw $$arez_exception$$;
    } catch( final Throwable $$arez_exception$$ ) {
      throw new IllegalStateException( $$arez_exception$$ );
    }
  }

  @Override
  public int someValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    return this.$$arez$$_someValue.get();
  }

  @Override
  public void render() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    try {
      $$arezi$$_context().safeTrack( this.$$arez$$_render, () -> super.render() );
    } catch( final RuntimeException | Error $$arez_exception$$ ) {
      throw $$arez_exception$$;
    } catch( final Throwable $$arez_exception$$ ) {
      throw new IllegalStateException( $$arez_exception$$ );
    }
  }

  @Override
  public final int hashCode() {
    return Long.hashCode( $$arezi$$_id() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_ImplicitSingletonModel) ) {
      return false;
    } else {
      final Arez_ImplicitSingletonModel that = (Arez_ImplicitSingletonModel) o;;
      return $$arezi$$_id() == that.$$arezi$$_id();
    }
  }

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + $$arezi$$_name() + "]";
    } else {
      return super.toString();
    }
  }
}
