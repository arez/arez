import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.arez.Observer;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
public final class Arez_SingletonModel extends SingletonModel implements Disposable {
  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_time;

  @Nonnull
  private final ComputedValue<Integer> $$arez$$_someValue;

  @Nonnull
  private final Observer $$arez$$_myAutorun;

  @Nonnull
  private final Observer $$arez$$_render;

  public Arez_SingletonModel() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_time = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? "SingletonModel.time" : null );
    this.$$arez$$_someValue = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? "SingletonModel.someValue" : null, super::someValue, Objects::equals, null, null, null, null );
    this.$$arez$$_myAutorun = this.$$arez$$_context.autorun( this.$$arez$$_context.areNamesEnabled() ? "SingletonModel.myAutorun" : null, true, () -> super.myAutorun(), false );
    this.$$arez$$_render = this.$$arez$$_context.tracker( this.$$arez$$_context.areNamesEnabled() ? "SingletonModel.render" : null, true, super::onRenderDepsUpdated );
    this.$$arez$$_context.triggerScheduler();
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      $$arez$$_myAutorun.dispose();
      $$arez$$_render.dispose();
      $$arez$$_someValue.dispose();
      $$arez$$_time.dispose();
    }
  }

  @Override
  public long getTime() {
    assert !$$arez$$_disposed;
    this.$$arez$$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime(final long time) {
    assert !$$arez$$_disposed;
    if ( time != super.getTime() ) {
      super.setTime(time);
      this.$$arez$$_time.reportChanged();
    }
  }

  @Override
  public void myAutorun() {
    assert !$$arez$$_disposed;
    this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? "SingletonModel.myAutorun" : null, true, () -> super.myAutorun() );
  }

  @Override
  public void doStuff(final long time) {
    assert !$$arez$$_disposed;
    try {
      this.$$arez$$_context.safeAction(this.$$arez$$_context.areNamesEnabled() ? "SingletonModel.doStuff" : null, true, () -> super.doStuff(time), time );
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
    assert !$$arez$$_disposed;
    return this.$$arez$$_someValue.get();
  }

  @Override
  public void render() {
    assert !$$arez$$_disposed;
    assert !$$arez$$_disposed;
    try {
      this.$$arez$$_context.safeTrack( this.$$arez$$_render, () -> super.render() );
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
  public final String toString() {
    if ( $$arez$$_context.areNamesEnabled() ) {
      return "ArezComponent[SingletonModel]";
    } else {
      return super.toString();
    }
  }
}
