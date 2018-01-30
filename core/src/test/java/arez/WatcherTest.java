package arez;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class WatcherTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Observable observable = context.createObservable();

    final AtomicBoolean result = new AtomicBoolean();

    final AtomicInteger conditionRun = new AtomicInteger();
    final AtomicInteger effectRun = new AtomicInteger();

    final String name = ValueUtil.randomString();
    final boolean mutation = true;
    final SafeFunction<Boolean> condition = () -> {
      conditionRun.incrementAndGet();
      observable.reportObserved();
      return result.get();
    };
    final SafeProcedure procedure = effectRun::incrementAndGet;

    final Watcher watcher = new Watcher( context, name, mutation, condition, procedure );

    assertEquals( watcher.getName(), name );
    assertEquals( watcher.isMutation(), mutation );
    assertEquals( watcher.getEffect(), procedure );

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );

    context.action( ValueUtil.randomString(), true, observable::reportChanged );

    assertEquals( conditionRun.get(), 2 );
    assertEquals( effectRun.get(), 0 );

    result.set( true );

    // Reschedule and run effect
    context.action( ValueUtil.randomString(), true, observable::reportChanged );

    assertEquals( conditionRun.get(), 3 );
    assertEquals( effectRun.get(), 1 );

    // Watcher should not be active anymore so this should do nothing
    context.action( ValueUtil.randomString(), true, observable::reportChanged );

    assertEquals( conditionRun.get(), 3 );
    assertEquals( effectRun.get(), 1 );
  }

  @Test
  public void dispose_releasesResources()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Observable observable = context.createObservable();

    final AtomicBoolean result = new AtomicBoolean();

    final AtomicInteger conditionRun = new AtomicInteger();
    final AtomicInteger effectRun = new AtomicInteger();

    final String name = ValueUtil.randomString();
    final boolean mutation = true;
    final SafeFunction<Boolean> condition = () -> {
      conditionRun.incrementAndGet();
      observable.reportObserved();
      return result.get();
    };
    final SafeProcedure procedure = effectRun::incrementAndGet;

    final Watcher watcher = new Watcher( context, name, mutation, condition, procedure );

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
    assertEquals( Disposable.isDisposed( watcher ), false );

    result.set( true );
    Disposable.dispose( watcher );

    assertEquals( Disposable.isDisposed( watcher ), true );

    context.action( ValueUtil.randomString(), true, observable::reportChanged );

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
  }

  @Test
  public void conditionReadOnlyTransaction()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger errorCount = new AtomicInteger();

    final ArrayList<Observer> observersErrored = new ArrayList<>();

    final ObserverErrorHandler handler = ( observer, error, throwable ) -> {
      errorCount.incrementAndGet();
      assertEquals( ObserverError.REACTION_ERROR, error );
      observersErrored.add( observer );
    };
    context.addObserverErrorHandler( handler );

    final Observable observable = context.createObservable();

    final AtomicBoolean result = new AtomicBoolean();

    final AtomicInteger conditionRun = new AtomicInteger();
    final AtomicInteger effectRun = new AtomicInteger();

    final String name = ValueUtil.randomString();
    final boolean mutation = true;
    final SafeFunction<Boolean> condition = () -> {
      conditionRun.incrementAndGet();
      observable.reportObserved();
      observable.reportChanged();
      return result.get();
    };
    final SafeProcedure procedure = effectRun::incrementAndGet;

    new Watcher( context, name, mutation, condition, procedure );

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
    assertEquals( errorCount.get(), 2 );

    //First error is the computed
    assertEquals( context.getSpy().isComputedValue( observersErrored.get( 0 ) ), true );
    //Second error was the autorun that called the computed
    assertEquals( context.getSpy().isComputedValue( observersErrored.get( 1 ) ), false );

    result.set( true );

    // Attempt to run condition. Should produce another error and condition evaluation
    context.action( ValueUtil.randomString(), true, observable::reportChanged );

    assertEquals( conditionRun.get(), 2 );
    assertEquals( effectRun.get(), 0 );
    assertEquals( errorCount.get(), 3 );

    //Next error is the computed again
    assertEquals( context.getSpy().isComputedValue( observersErrored.get( 0 ) ), true );
  }

  @Test
  public void verifyEffectWhenReadOnlyTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final AtomicInteger errorCount = new AtomicInteger();

    final ObserverErrorHandler handler = ( observer, error, throwable ) -> errorCount.incrementAndGet();
    context.addObserverErrorHandler( handler );

    final Observable observable = context.createObservable();

    final AtomicInteger effectRun = new AtomicInteger();

    final SafeProcedure effect = () -> {
      effectRun.incrementAndGet();
      observable.reportObserved();
      observable.reportChanged();
    };

    new Watcher( context, ValueUtil.randomString(), false, () -> true, effect );

    assertEquals( effectRun.get(), 1 );
    assertEquals( errorCount.get(), 1 );
  }

  @Test
  public void verifyEffectWhenReadWriteTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final AtomicInteger errorCount = new AtomicInteger();

    final ObserverErrorHandler handler = ( observer, error, throwable ) -> errorCount.incrementAndGet();
    context.addObserverErrorHandler( handler );

    final Observable observable = context.createObservable();

    final AtomicInteger effectRun = new AtomicInteger();

    final SafeProcedure effect = () -> {
      effectRun.incrementAndGet();
      observable.reportObserved();
      observable.reportChanged();
    };

    new Watcher( context, ValueUtil.randomString(), true, () -> true, effect );

    assertEquals( effectRun.get(), 1 );
    assertEquals( errorCount.get(), 0 );
  }
}
