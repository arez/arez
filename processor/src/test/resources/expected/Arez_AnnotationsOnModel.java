import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.braincheck.Guards;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
public final class Arez_AnnotationsOnModel extends AnnotationsOnModel implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_time;

  @Nonnull
  private final ComputedValue<Integer> $$arez$$_someValue;

  public Arez_AnnotationsOnModel() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_time = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".time" : null );
    this.$$arez$$_someValue = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".someValue" : null, super::someValue, Objects::equals, null, null, null, null );
  }

  final long $$arez$$_id() {
    return $$arez$$_id;
  }

  String $$arez$$_name() {
    return "AnnotationsOnModel." + $$arez$$_id();
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      $$arez$$_someValue.dispose();
      $$arez$$_time.dispose();
    }
  }

  @Nonnull
  @Override
  public String getTime() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    this.$$arez$$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime(@Nonnull final String time) {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    if ( !Objects.equals(time, super.getTime()) ) {
      super.setTime(time);
      this.$$arez$$_time.reportChanged();
    }
  }

  @Override
  public void doStuff(@Nonnull final String time) {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    try {
      this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".doStuff" : null, true, () -> super.doStuff(time), time );
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

  @Nonnull
  @Override
  public Integer someValue() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    return this.$$arez$$_someValue.get();
  }

  @Override
  public final int hashCode() {
    return Long.hashCode( $$arez$$_id() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_AnnotationsOnModel) ) {
      return false;
    } else {
      final Arez_AnnotationsOnModel that = (Arez_AnnotationsOnModel) o;;
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
