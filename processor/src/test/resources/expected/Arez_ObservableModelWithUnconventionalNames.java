import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Observable;
import arez.component.Identifiable;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public final class Arez_ObservableModelWithUnconventionalNames extends ObservableModelWithUnconventionalNames implements Disposable, Identifiable<Long> {
  private static volatile long $$arezi$$_nextId;

  private final long $$arezi$$_id;

  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final Observable<Boolean> $$arezi$$_disposedObservable;

  @Nonnull
  private final Observable<Long> $$arez$$_time;

  public Arez_ObservableModelWithUnconventionalNames() {
    super();
    this.$$arezi$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arezi$$_id = $$arezi$$_nextId++;
    this.$$arezi$$_state = 1;
    this.$$arezi$$_component = Arez.areNativeComponentsEnabled() ? $$arezi$$_context().createComponent( "ObservableModelWithUnconventionalNames", $$arezi$$_id(), $$arezi$$_name(), null, null ) : null;
    this.$$arezi$$_disposedObservable = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezi$$_state >= 0 : null, null );
    this.$$arez$$_time = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".time" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.time() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.time( v ) : null );
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arezi$$_component.complete();
    }
    this.$$arezi$$_state = 2;
    this.$$arezi$$_state = 3;
  }

  final ArezContext $$arezi$$_context() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arezi$$_state == 0, () -> "Method invoked on uninitialized component named '" + $$arezi$$_name() + "'" );
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
      Guards.apiInvariant( () -> this.$$arezi$$_state == 0, () -> "Method invoked on uninitialized component named '" + $$arezi$$_name() + "'" );
    }
    return "ObservableModelWithUnconventionalNames." + $$arezi$$_id();
  }

  @Override
  public boolean isDisposed() {
    if ( $$arezi$$_context().isTransactionActive() && !this.$$arezi$$_disposedObservable.isDisposed() )  {
      this.$$arezi$$_disposedObservable.reportObserved();
    }
    return this.$$arezi$$_state < 0;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      this.$$arezi$$_state = -2;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arezi$$_component.dispose();
      } else {
        $$arezi$$_context().safeAction( Arez.areNamesEnabled() ? $$arezi$$_name() + ".dispose" : null, () -> { {
          this.$$arezi$$_disposedObservable.dispose();
          this.$$arez$$_time.dispose();
        } } );
      }
      this.$$arezi$$_state = -1;
    }
  }

  @Override
  public long time() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arezi$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arezi$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_time.reportObserved();
    return super.time();
  }

  @Override
  public void time(final long time) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arezi$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arezi$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( time != super.time() ) {
      this.$$arez$$_time.preReportChanged();
      super.time(time);
      this.$$arez$$_time.reportChanged();
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
    } else if ( null == o || !(o instanceof Arez_ObservableModelWithUnconventionalNames) ) {
      return false;
    } else {
      final Arez_ObservableModelWithUnconventionalNames that = (Arez_ObservableModelWithUnconventionalNames) o;;
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
