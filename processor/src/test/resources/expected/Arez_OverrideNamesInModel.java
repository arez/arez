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
import org.realityforge.arez.spy.ActionCompletedEvent;
import org.realityforge.arez.spy.ActionStartedEvent;

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
  public void zapZap() {
    this.$$arez$$_context.safeProcedure(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".zzzzzz" : null, true, () -> super.zapZap() );
  }

  @Override
  public void doAction() {
    assert !$$arez$$_disposed;
    Throwable $$arez$$_throwable = null;
    boolean $$arez$$_completed = false;
    long $$arez$$_startedAt = 0L;
    try {
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        $$arez$$_startedAt = System.currentTimeMillis();
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionStartedEvent( $$arez$$_name() + ".myAction", false, new Object[]{} ) );
      }
      this.$$arez$$_context.safeProcedure(this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".myAction" : null, true, () -> super.doAction() );
      $$arez$$_completed = true;
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_name() + ".myAction", false, new Object[]{}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
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
          this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_name() + ".myAction", false, new Object[]{}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
        }
      }
    }
  }

  @Override
  int compute() {
    return this.$$arez$$_myComputed.get();
  }

  @Override
  public void render() throws ParseException {
    assert !$$arez$$_disposed;
    Throwable $$arez$$_throwable = null;
    boolean $$arez$$_completed = false;
    long $$arez$$_startedAt = 0L;
    try {
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        $$arez$$_startedAt = System.currentTimeMillis();
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionStartedEvent( $$arez$$_name() + ".XX", true, new Object[]{} ) );
      }
      this.$$arez$$_context.procedure( this.$$arez$$_XX, () -> super.render() );
      $$arez$$_completed = true;
      if ( this.$$arez$$_context.areSpiesEnabled() && this.$$arez$$_context.getSpy().willPropagateSpyEvents() ) {
        final long $$arez$$_duration = System.currentTimeMillis() - $$arez$$_startedAt;
        this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_name() + ".XX", true, new Object[]{}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
      }
    } catch( final ParseException e ) {
      throw e;
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
          this.$$arez$$_context.getSpy().reportSpyEvent( new ActionCompletedEvent( $$arez$$_name() + ".XX", true, new Object[]{}, false, null, $$arez$$_throwable, $$arez$$_duration ) );
        }
      }
    }
  }
}
