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
public final class Arez_AnnotationsOnModel extends AnnotationsOnModel implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_time;

  @Nonnull
  private final ComputedValue<Integer> $$arez$$_someValue;

  public Arez_AnnotationsOnModel(@Nonnull final ArezContext $$arez$$_context) {
    super();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_time = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "time" : null );
    this.$$arez$$_someValue = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "someValue" : null, super::someValue, Objects::equals );
  }

  private String $$arez$$_id() {
    return "AnnotationsOnModel." + $$arez$$_id + ".";
  }

  @Override
  public void dispose() {
    $$arez$$_someValue.dispose();
    $$arez$$_time.dispose();
  }

  @Nonnull
  @Override
  public String getTime() {
    this.$$arez$$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime(@Nonnull final String time) {
    if ( !Objects.equals(time, super.getTime()) ) {
      super.setTime(time);
      this.$$arez$$_time.reportChanged();
    }
  }

  @Override
  public void doStuff(@Nonnull final String time) {
    Throwable $$arez$$_throwable = null;
    boolean $$arez$$_completed = false;
    long $$arez$$_startedAt = 0L;
    try {
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        $$arez$$_startedAt = System.currentTimeMillis();
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionStartedEvent( $$arez$$_id() + "doStuff", new Object[]{time} ) );
      }
      this.$$arez$$_context.safeProcedure(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "doStuff" : null, true, () -> super.doStuff(time) );
      $$arez$$_completed = true;
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_id() + "doStuff", new Object[]{time}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
      }
    } catch( final RuntimeException e ) {
      $$arez$$_throwable = e;
      throw e;
    } catch( final Exception e ) {
      $$arez$$_throwable = e;
      throw new UndeclaredThrowableException( e );
    } catch( final Error e ) {
      $$arez$$_throwable = e;
      throw e;
    } catch( final Throwable e ) {
      $$arez$$_throwable = e;
      throw new UndeclaredThrowableException( e );
    } finally {
      if ( !$$arez$$_completed ) {
        if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
          final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
          this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_id() + "doStuff", new Object[]{time}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
        }
      }
    }
  }

  @Nonnull
  @Override
  public Integer someValue() {
    return this.$$arez$$_someValue.get();
  }
}
