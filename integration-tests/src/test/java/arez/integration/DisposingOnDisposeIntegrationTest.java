package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.ComputedValue;
import arez.Disposable;
import arez.ObservableValue;
import arez.Observer;
import arez.Priority;
import arez.Procedure;
import arez.SafeFunction;
import arez.SafeProcedure;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DisposingOnDisposeIntegrationTest
  extends AbstractArezIntegrationTest
{
  static final class Watcher
    implements Disposable
  {
    private final ComputedValue<Boolean> _condition;
    private final SafeProcedure _effect;
    private final Observer _observer;

    Watcher( @Nonnull final SafeFunction<Boolean> condition, @Nonnull final SafeProcedure effect )
    {
      _effect = effect;
      final ArezContext context = Arez.context();
      _condition = context.computed( null, null, condition, null, this::dispose, null, null, Priority.NORMAL );
      final Procedure onDispose = () -> Disposable.dispose( _condition );
      _observer =
        context.autorun( null, null, true, this::checkCondition, Priority.NORMAL, false, true, true, onDispose );
      context.triggerScheduler();
    }

    Observer getObserver()
    {
      return _observer;
    }

    private void checkCondition()
    {
      if ( Disposable.isNotDisposed( _condition ) && _condition.get() )
      {
        Arez.context().safeAction( (String) null, true, true, _effect );
        Disposable.dispose( _observer );
      }
    }

    @Override
    public void dispose()
    {
      Disposable.dispose( _observer );
    }

    @Override
    public boolean isDisposed()
    {
      return Disposable.isDisposed( _observer );
    }

    ComputedValue<Boolean> getCondition()
    {
      return _condition;
    }
  }

  @Test
  public void dispose_releasesResources()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ObservableValue observable = context.observable();

    final AtomicBoolean result = new AtomicBoolean();

    final AtomicInteger conditionRun = new AtomicInteger();
    final AtomicInteger effectRun = new AtomicInteger();

    final SafeFunction<Boolean> condition = () -> {
      conditionRun.incrementAndGet();
      observable.reportObserved();
      return result.get();
    };
    final SafeProcedure procedure = effectRun::incrementAndGet;

    final Watcher watcher = new Watcher( condition, procedure );

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
    assertEquals( Disposable.isDisposed( watcher ), false );
    assertEquals( Disposable.isDisposed( watcher.getObserver() ), false );
    assertEquals( Disposable.isDisposed( watcher.getCondition() ), false );

    result.set( true );
    Disposable.dispose( watcher );

    assertEquals( Disposable.isDisposed( watcher ), true );
    assertEquals( Disposable.isDisposed( watcher.getObserver() ), true );
    assertEquals( Disposable.isDisposed( watcher.getCondition() ), true );

    context.action( ValueUtil.randomString(), true, observable::reportChanged );

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
  }
}
