import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_ObservableModelWithUnconventionalNames extends ObservableModelWithUnconventionalNames implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_time;

  public Arez_ObservableModelWithUnconventionalNames() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_time = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".time" : null );
  }

  final long $$arez$$_id() {
    return $$arez$$_id;
  }

  String $$arez$$_name() {
    return "ObservableModelWithUnconventionalNames." + $$arez$$_id();
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      $$arez$$_time.dispose();
    }
  }

  @Override
  public long time() {
    assert !$$arez$$_disposed;
    this.$$arez$$_time.reportObserved();
    return super.time();
  }

  @Override
  public void time(final long time) {
    assert !$$arez$$_disposed;
    if ( time != super.time() ) {
      super.time(time);
      this.$$arez$$_time.reportChanged();
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
    } else if ( null == o || !(o instanceof Arez_ObservableModelWithUnconventionalNames) ) {
      return false;
    } else {
      final Arez_ObservableModelWithUnconventionalNames that = (Arez_ObservableModelWithUnconventionalNames) o;;
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
