import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.arez.Observer;
import org.realityforge.arez.spy.ActionCompletedEvent;
import org.realityforge.arez.spy.ActionStartedEvent;

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
    this.$$arez$$_render = this.$$arez$$_context.reaction( this.$$arez$$_context.areNamesEnabled() ? "SingletonModel.render" : null, true, super::onRenderDepsUpdated );
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
    this.$$arez$$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime(final long time) {
    if ( time != super.getTime() ) {
      super.setTime(time);
      this.$$arez$$_time.reportChanged();
    }
  }

  @Override
  public void myAutorun() {
    this.$$arez$$_context.safeProcedure(this.$$arez$$_context.areNamesEnabled() ? "SingletonModel.myAutorun" : null, true, () -> super.myAutorun() );
  }

  @Override
  public void doStuff(final long time) {
    assert !$$arez$$_disposed;
    Throwable $$arez$$_throwable = null;
    boolean $$arez$$_completed = false;
    long $$arez$$_startedAt = 0L;
    try {
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        $$arez$$_startedAt = System.currentTimeMillis();
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionStartedEvent( "SingletonModel.doStuff", false, new Object[]{time} ) );
      }
      this.$$arez$$_context.safeProcedure(this.$$arez$$_context.areNamesEnabled() ? "SingletonModel.doStuff" : null, true, () -> super.doStuff(time) );
      $$arez$$_completed = true;
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( "SingletonModel.doStuff", false, new Object[]{time}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
      }
    } catch( final RuntimeException e ) {
      $$arez$$_throwable = e;
      throw e;
    } catch( final Exception e ) {
      $$arez$$_throwable = e;
      throw new IllegalStateException( e );
    } catch( final Error e ) {
      $$arez$$_throwable = e;
      throw e;
    } catch( final Throwable e ) {
      $$arez$$_throwable = e;
      throw new IllegalStateException( e );
    } finally {
      if ( !$$arez$$_completed ) {
        if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
          final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
          this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( "SingletonModel.doStuff", false, new Object[]{time}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
        }
      }
    }
  }

  @Override
  public int someValue() {
    return this.$$arez$$_someValue.get();
  }

  @Override
  public void render() {
    assert !$$arez$$_disposed;
    Throwable $$arez$$_throwable = null;
    boolean $$arez$$_completed = false;
    long $$arez$$_startedAt = 0L;
    try {
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        $$arez$$_startedAt = System.currentTimeMillis();
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionStartedEvent( "SingletonModel.render", true, new Object[]{} ) );
      }
      this.$$arez$$_context.safeProcedure( this.$$arez$$_render, () -> super.render() );
      $$arez$$_completed = true;
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( "SingletonModel.render", true, new Object[]{}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
      }
    } catch( final RuntimeException e ) {
      $$arez$$_throwable = e;
      throw e;
    } catch( final Exception e ) {
      $$arez$$_throwable = e;
      throw new IllegalStateException( e );
    } catch( final Error e ) {
      $$arez$$_throwable = e;
      throw e;
    } catch( final Throwable e ) {
      $$arez$$_throwable = e;
      throw new IllegalStateException( e );
    } finally {
      if ( !$$arez$$_completed ) {
        if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
          final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
          this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( "SingletonModel.render", true, new Object[]{}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
        }
      }
    }
  }
}
