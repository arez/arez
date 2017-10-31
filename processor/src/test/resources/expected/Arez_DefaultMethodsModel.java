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
public final class Arez_DefaultMethodsModel extends DefaultMethodsModel implements Disposable {
  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_time;

  @Nonnull
  private final ComputedValue<Integer> $$arez$$_someValue;

  public Arez_DefaultMethodsModel() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_time = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? "DefaultMethodsModel.time" : null );
    this.$$arez$$_someValue = this.$$arez$$_context.createComputedValue( Arez.areNamesEnabled() ? "DefaultMethodsModel.someValue" : null, super::someValue, Objects::equals, null, null, null, null );
  }

  @Override
  public boolean isDisposed() {
    return this.$$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      this.$$arez$$_disposed = true;
      this.$$arez$$_context.safeAction( Arez.areNamesEnabled() ? "DefaultMethodsModel.dispose" : null, () -> { {
        this.$$arez$$_someValue.dispose();
        this.$$arez$$_time.dispose();
      } } );
    }
  }

  @Override
  public long getTime() {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component 'DefaultMethodsModel'" );
    this.$$arez$$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime(final long time) {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component 'DefaultMethodsModel'" );
    if ( time != super.getTime() ) {
      super.setTime(time);
      this.$$arez$$_time.reportChanged();
    }
  }

  @Override
  public void doStuff(final long time) {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component 'DefaultMethodsModel'" );
    try {
      this.$$arez$$_context.safeAction(Arez.areNamesEnabled() ? "DefaultMethodsModel.doStuff" : null, true, () -> super.doStuff(time), time );
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
  public int someValue() {
    Guards.invariant( () -> !this.$$arez$$_disposed, () -> "Method invoked on invalid component 'DefaultMethodsModel'" );
    return this.$$arez$$_someValue.get();
  }

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[DefaultMethodsModel]";
    } else {
      return super.toString();
    }
  }
}
