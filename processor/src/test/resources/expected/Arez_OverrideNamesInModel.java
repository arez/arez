import java.text.ParseException;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.arez.Observer;
import org.realityforge.braincheck.Guards;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
public final class Arez_OverrideNamesInModel extends OverrideNamesInModel implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_myField;

  @Nonnull
  private final ComputedValue<Integer> $$arez$$_myComputed;

  @Nonnull
  private final Observer $$arez$$_zzzzzz;

  @Nonnull
  private final Observer $$arez$$_XX;

  public Arez_OverrideNamesInModel() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_myField = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".myField" : null );
    this.$$arez$$_myComputed = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".myComputed" : null, super::compute, Objects::equals, null, null, null, null );
    this.$$arez$$_zzzzzz = this.$$arez$$_context.autorun( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".zzzzzz" : null, true, () -> super.zapZap(), false );
    this.$$arez$$_XX = this.$$arez$$_context.tracker( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".XX" : null, true, super::onRenderDepsUpdated );
    this.$$arez$$_context.triggerScheduler();
  }

  final long $$arez$$_id() {
    return $$arez$$_id;
  }

  String $$arez$$_name() {
    return "MyContainer." + $$arez$$_id();
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      $$arez$$_zzzzzz.dispose();
      $$arez$$_XX.dispose();
      $$arez$$_myComputed.dispose();
      $$arez$$_myField.dispose();
    }
  }

  @Override
  public long getTime() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    this.$$arez$$_myField.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime(final long time) {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    if ( time != super.getTime() ) {
      super.setTime(time);
      this.$$arez$$_myField.reportChanged();
    }
  }

  @Override
  public void zapZap() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".zzzzzz" : null, true, () -> super.zapZap() );
  }

  @Override
  public void doAction() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    try {
      this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".myAction" : null, true, () -> super.doAction() );
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
  int compute() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    return this.$$arez$$_myComputed.get();
  }

  @Override
  public void render() throws ParseException {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component '" + $$arez$$_name() + "'" );
    try {
      this.$$arez$$_context.track( this.$$arez$$_XX, () -> super.render() );
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
    } else if ( null == o || !(o instanceof Arez_OverrideNamesInModel) ) {
      return false;
    } else {
      final Arez_OverrideNamesInModel that = (Arez_OverrideNamesInModel) o;;
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
