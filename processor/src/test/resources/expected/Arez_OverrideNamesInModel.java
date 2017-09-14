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
public final class Arez_OverrideNamesInModel extends OverrideNamesInModel implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_myField;

  @Nonnull
  private final ComputedValue<Integer> $$arez$$_myComputed;

  public Arez_OverrideNamesInModel(@Nonnull final ArezContext $$arez$$_context) {
    super();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_myField = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "myField" : null );
    this.$$arez$$_myComputed = this.$$arez$$_context.createComputedValue( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "myComputed" : null, super::compute, Objects::equals );
  }

  private String $$arez$$_id() {
    return "MyContainer." + $$arez$$_id + ".";
  }

  @Override
  public void dispose() {
    $$arez$$_myComputed.dispose();
    $$arez$$_myField.dispose();
  }

  @Override
  public long getTime() {
    this.$$arez$$_myField.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime(final long time) {
    if ( time != super.getTime() ) {
      super.setTime(time);
      this.$$arez$$_myField.reportChanged();
    }
  }

  @Override
  public void doAction() {
    Throwable $$arez$$_throwable = null;
    boolean $$arez$$_completed = false;
    long $$arez$$_startedAt = 0L;
    try {
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        $$arez$$_startedAt = System.currentTimeMillis();
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionStartedEvent( $$arez$$_id() + "myAction", new Object[]{} ) );
      }
      this.$$arez$$_context.safeProcedure(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "myAction" : null, true, () -> super.doAction() );
      $$arez$$_completed = true;
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_id() + "myAction", new Object[]{}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
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
          this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_id() + "myAction", new Object[]{}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
        }
      }
    }
  }

  @Override
  int compute() {
    return this.$$arez$$_myComputed.get();
  }
}
