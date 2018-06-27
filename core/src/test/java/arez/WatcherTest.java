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
    final Component component = context.createComponent( ValueUtil.randomString(), ValueUtil.randomString() );

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

    final Watcher watcher =
      new Watcher( context, component, name, mutation, condition, procedure, Priority.NORMAL, true );

    assertEquals( watcher.getName(), name );
    assertEquals( watcher.isMutation(), mutation );
    assertEquals( watcher.getEffect(), procedure );
    assertEquals( watcher.getWatcher().getName(), name + ".watcher" );
    assertEquals( watcher.getWatcher().getComponent(), component );
    assertEquals( watcher.getWatcher().getPriority(), Priority.NORMAL );

    assertEquals( watcher.getCondition().getName(), name + ".condition" );
    assertEquals( watcher.getCondition().getComponent(), component );

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
  public void basicOperation_noRunImmediately()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Observable observable = context.createObservable();

    final AtomicBoolean result = new AtomicBoolean();

    final AtomicInteger conditionRun = new AtomicInteger();
    final AtomicInteger effectRun = new AtomicInteger();

    final boolean mutation = true;
    final SafeFunction<Boolean> condition = () -> {
      conditionRun.incrementAndGet();
      observable.reportObserved();
      return result.get();
    };
    final SafeProcedure procedure = effectRun::incrementAndGet;

    new Watcher( context, null, ValueUtil.randomString(), mutation, condition, procedure, Priority.NORMAL, false );

    assertEquals( conditionRun.get(), 0 );
    assertEquals( effectRun.get(), 0 );

    context.triggerScheduler();

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
  }

  @Test
  public void basicOperation_highPriorityEnabled()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger conditionRun1 = new AtomicInteger();
    final AtomicInteger conditionRun2 = new AtomicInteger();
    final AtomicInteger conditionRun3 = new AtomicInteger();
    final ArrayList<String> results = new ArrayList<>();

    final SafeFunction<Boolean> condition1 = () -> {
      observeADependency();
      conditionRun1.incrementAndGet();
      results.add( "1" );
      return false;
    };
    final SafeFunction<Boolean> condition2 = () -> {
      observeADependency();
      conditionRun2.incrementAndGet();
      results.add( "2" );
      return false;
    };
    final SafeFunction<Boolean> condition3 = () -> {
      observeADependency();
      conditionRun3.incrementAndGet();
      results.add( "3" );
      return false;
    };
    final SafeProcedure procedure = () -> {
    };

    final Watcher watcher3 =
      new Watcher( context, null, ValueUtil.randomString(), true, condition3, procedure, Priority.NORMAL, false );
    final Watcher watcher2 =
      new Watcher( context, null, ValueUtil.randomString(), true, condition2, procedure, Priority.HIGHEST, false );
    final Watcher watcher1 =
      new Watcher( context, null, ValueUtil.randomString(), true, condition1, procedure, Priority.HIGHEST, false );

    assertEquals( watcher1.getWatcher().getPriority(), Priority.HIGHEST );
    assertEquals( watcher1.getCondition().getObserver().getPriority(), Priority.HIGHEST );
    assertEquals( watcher2.getWatcher().getPriority(), Priority.HIGHEST );
    assertEquals( watcher2.getCondition().getObserver().getPriority(), Priority.HIGHEST );
    assertEquals( watcher3.getWatcher().getPriority(), Priority.NORMAL );
    assertEquals( watcher3.getCondition().getObserver().getPriority(), Priority.NORMAL );

    assertEquals( conditionRun1.get(), 0 );
    assertEquals( conditionRun2.get(), 0 );
    assertEquals( conditionRun3.get(), 0 );

    context.triggerScheduler();

    assertEquals( conditionRun1.get(), 1 );
    assertEquals( conditionRun2.get(), 1 );
    assertEquals( conditionRun3.get(), 1 );

    assertEquals( results.size(), 3 );

    assertEquals( results.get( 0 ), "2" );
    assertEquals( results.get( 1 ), "1" );
    assertEquals( results.get( 2 ), "3" );
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

    final Watcher watcher = new Watcher( context, null, name, mutation, condition, procedure, Priority.NORMAL, true );

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
    assertEquals( Disposable.isDisposed( watcher ), false );
    assertEquals( Disposable.isDisposed( watcher.getWatcher() ), false );
    assertEquals( Disposable.isDisposed( watcher.getCondition() ), false );

    result.set( true );
    Disposable.dispose( watcher );

    assertEquals( Disposable.isDisposed( watcher ), true );
    assertEquals( Disposable.isDisposed( watcher.getWatcher() ), true );
    assertEquals( Disposable.isDisposed( watcher.getCondition() ), true );

    context.action( ValueUtil.randomString(), true, observable::reportChanged );

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
  }

  @Test
  public void dispose_of_observer_disposesCondition()
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

    final Watcher watcher = new Watcher( context, null, name, mutation, condition, procedure, Priority.NORMAL, true );

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
    assertEquals( Disposable.isDisposed( watcher ), false );

    result.set( true );
    Disposable.dispose( watcher.getWatcher() );

    assertEquals( Disposable.isDisposed( watcher ), true );
    assertEquals( Disposable.isDisposed( watcher.getWatcher() ), true );
    assertEquals( Disposable.isDisposed( watcher.getCondition() ), true );

    context.action( ValueUtil.randomString(), true, observable::reportChanged );

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
  }

  @Test
  public void conditionReadOnlyTransaction()
    throws Throwable
  {
    setIgnoreObserverErrors( true );
    setPrintObserverErrors( false );

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

    new Watcher( context, null, name, mutation, condition, procedure, Priority.NORMAL, true );

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
    setIgnoreObserverErrors( true );
    setPrintObserverErrors( false );

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

    final SafeFunction<Boolean> function = () -> {
      observeADependency();
      return true;
    };
    new Watcher( context, null, ValueUtil.randomString(), false, function, effect, Priority.NORMAL, true );

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

    final SafeFunction<Boolean> function = () -> {
      observeADependency();
      return true;
    };
    new Watcher( context, null, ValueUtil.randomString(), true, function, effect, Priority.NORMAL, true );

    assertEquals( effectRun.get(), 1 );
    assertEquals( errorCount.get(), 0 );
  }
}
