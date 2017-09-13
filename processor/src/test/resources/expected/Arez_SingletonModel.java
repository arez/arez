import java.lang.reflect.UndeclaredThrowableException;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.arez.spy.ActionCompletedEvent;
import org.realityforge.arez.spy.ActionStartedEvent;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_SingletonModel extends SingletonModel implements Disposable {
  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_time;

  @Nonnull
  private final ComputedValue<Integer> $$arez$$_someValue;

  public Arez_SingletonModel(@Nonnull final ArezContext $$arez$$_context) {
    super();
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_time = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? "SingletonModel.time" : null );
    this.$$arez$$_someValue = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? "SingletonModel.someValue" : null, super::someValue, Objects::equals );
  }

  @Override
  public void dispose() {
    $$arez$$_someValue.dispose();
    $$arez$$_time.dispose();
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
  public void doStuff(final long time) {
    boolean $$arez$$_completed = false;
    long $$arez$$_startedAt = 0L;
    try {
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        $$arez$$_startedAt = System.currentTimeMillis();
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionStartedEvent( "SingletonModel.doStuff", new Object[]{time} ) );
      }
      this.$$arez$$_context.safeProcedure(this.$$arez$$_context.areNamesEnabled() ? "SingletonModel.doStuff" : null, true, () -> super.doStuff(time) );
      $$arez$$_completed = true;
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( "SingletonModel.doStuff", new Object[]{time}, null, $$arez$$_duration ) );
      }
    } catch( final RuntimeException e ) {
      throw e;
    } catch( final Exception e ) {
      throw new UndeclaredThrowableException( e );
    } catch( final Error e ) {
      throw e;
    } catch( final Throwable e ) {
      throw new UndeclaredThrowableException( e );
    } finally {
      if ( !$$arez$$_completed ) {
        final Void $$arez$$_result = null;
        if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
          final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
          this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( "SingletonModel.doStuff", new Object[]{time}, null, $$arez$$_duration ) );
        }
      }
    }
  }

  @Override
  public int someValue() {
    return this.$$arez$$_someValue.get();
  }
}
