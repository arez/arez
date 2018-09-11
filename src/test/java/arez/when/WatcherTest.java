package arez.when;

import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.Component;
import arez.Disposable;
import arez.Flags;
import arez.ObservableValue;
import arez.Observer;
import arez.ObserverError;
import arez.ObserverErrorHandler;
import arez.SafeFunction;
import arez.SafeProcedure;
import arez.spy.ComponentInfo;
import arez.spy.ObserverInfo;
import arez.spy.Priority;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class WatcherTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ObservableValue observable = context.observable();
    final ObservableValue observable2 = context.observable();
    final Component component = context.component( ValueUtil.randomString(), ValueUtil.randomString() );

    final AtomicBoolean result = new AtomicBoolean();

    final AtomicInteger conditionRun = new AtomicInteger();
    final AtomicInteger effectRun = new AtomicInteger();

    final String name = ValueUtil.randomString();
    final boolean mutation = true;
    final boolean verifyActionRequired = true;
    final SafeFunction<Boolean> condition = () -> {
      conditionRun.incrementAndGet();
      observable.reportObserved();
      return result.get();
    };
    final SafeProcedure procedure = () -> {
      observable2.reportObserved();
      effectRun.incrementAndGet();
    };

    final Watcher watcher =
      new Watcher( context,
                   component,
                   name,
                   mutation,
                   verifyActionRequired,
                   condition,
                   procedure,
                   Flags.PRIORITY_NORMAL,
                   true );

    assertEquals( watcher.getContext(), context );
    assertEquals( watcher.getName(), name );
    assertEquals( watcher.toString(), name );
    assertEquals( watcher.isMutation(), mutation );
    assertEquals( watcher.getEffect(), procedure );
    assertEquals( watcher.getObserver().getName(), name + ".watcher" );
    final ComponentInfo watcherComponentInfo = context.getSpy().asObserverInfo( watcher.getObserver() ).getComponent();
    assertNotNull( watcherComponentInfo );
    assertEquals( watcherComponentInfo.getId(), component.getId() );
    final ObserverInfo observerInfo = context.getSpy().asObserverInfo( watcher.getObserver() );
    assertEquals( observerInfo.getPriority(), Priority.NORMAL );

    final ComponentInfo conditionComponentInfo =
      context.getSpy().asComputedValueInfo( watcher.getCondition() ).getComponent();
    assertNotNull( conditionComponentInfo );
    assertEquals( conditionComponentInfo.getId(), component.getId() );

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

    new Watcher( context,
                 null,
                 ValueUtil.randomString(),
                 true,
                 true,
                 condition,
                 procedure,
                 Flags.PRIORITY_NORMAL,
                 false );

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
      observeDependency();
      conditionRun1.incrementAndGet();
      results.add( "1" );
      return false;
    };
    final SafeFunction<Boolean> condition2 = () -> {
      observeDependency();
      conditionRun2.incrementAndGet();
      results.add( "2" );
      return false;
    };
    final SafeFunction<Boolean> condition3 = () -> {
      observeDependency();
      conditionRun3.incrementAndGet();
      results.add( "3" );
      return false;
    };
    final SafeProcedure procedure = () -> {
    };

    final Watcher watcher3 =
      new Watcher( context,
                   null,
                   ValueUtil.randomString(),
                   true,
                   true,
                   condition3,
                   procedure,
                   Flags.PRIORITY_NORMAL,
                   false );
    final Watcher watcher2 =
      new Watcher( context,
                   null,
                   ValueUtil.randomString(),
                   true,
                   true,
                   condition2,
                   procedure,
                   Flags.PRIORITY_HIGH,
                   false );
    final Watcher watcher1 =
      new Watcher( context,
                   null,
                   ValueUtil.randomString(),
                   true,
                   true,
                   condition1,
                   procedure,
                   Flags.PRIORITY_HIGH,
                   false );

    assertEquals( context.getSpy().asObserverInfo( watcher1.getObserver() ).getPriority(), Priority.HIGH );

    assertEquals( context.getSpy().asObserverInfo( watcher1.getObserver() ).getPriority(), Priority.HIGH );
    assertEquals( context.getSpy().asObserverInfo( watcher2.getObserver() ).getPriority(), Priority.HIGH );
    assertEquals( context.getSpy().asObserverInfo( watcher3.getObserver() ).getPriority(), Priority.NORMAL );

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
  public void noNameSuppliedWhenNamesDisabled()
    throws Exception
  {
    ArezTestUtil.disableNames();

    final ArezContext context = Arez.context();

    final Watcher watcher =
      new Watcher( context,
                   context.component( ValueUtil.randomString(), ValueUtil.randomString() ),
                   null,
                   false,
                   false,
                   () -> {
                     observeDependency();
                     return false;
                   },
                   () -> {
                   },
                   Flags.PRIORITY_NORMAL,
                   true );

    assertTrue( watcher.toString().startsWith( "arez.when.Watcher@" ), "watcher.toString() == " + watcher );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, watcher::getName );
    assertEquals( exception.getMessage(), "Watcher.getName() invoked when Arez.areNamesEnabled() is false" );
  }

  @Test
  public void nameSuppliedWhenNamesDisabled()
    throws Exception
  {
    ArezTestUtil.disableNames();

    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Watcher( context,
                                       context.component( ValueUtil.randomString(), ValueUtil.randomString() ),
                                       name,
                                       false,
                                       false,
                                       () -> {
                                         observeDependency();
                                         return false;
                                       },
                                       () -> {
                                       },
                                       Flags.PRIORITY_NORMAL,
                                       true ) );

    assertEquals( exception.getMessage(),
                  "Watcher passed a name '" + name + "' but Arez.areNamesEnabled() is false" );
  }

  @Test
  public void contextSuppliedWhenZonesDisabled()
    throws Exception
  {
    ArezTestUtil.disableZones();

    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Watcher( context,
                                       context.component( ValueUtil.randomString(), ValueUtil.randomString() ),
                                       name,
                                       false,
                                       false,
                                       () -> {
                                         observeDependency();
                                         return false;
                                       },
                                       () -> {
                                       },
                                       Flags.PRIORITY_NORMAL,
                                       true ) );

    assertEquals( exception.getMessage(),
                  "Watcher passed a context but Arez.areZonesEnabled() is false" );
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

    final String name = ValueUtil.randomString();
    final SafeFunction<Boolean> condition = () -> {
      conditionRun.incrementAndGet();
      observable.reportObserved();
      return result.get();
    };
    final SafeProcedure procedure = effectRun::incrementAndGet;

    final Watcher
      watcher = new Watcher( context, null, name, true, true, condition, procedure, Flags.PRIORITY_NORMAL, true );

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
    assertEquals( Disposable.isDisposed( watcher ), false );
    assertEquals( Disposable.isDisposed( watcher.getObserver() ), false );

    result.set( true );
    Disposable.dispose( watcher );

    assertEquals( Disposable.isDisposed( watcher ), true );
    assertEquals( Disposable.isDisposed( watcher.getObserver() ), true );

    context.action( ValueUtil.randomString(), true, observable::reportChanged );

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
  }

  @Test
  public void dispose_of_observer_disposesCondition()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ObservableValue observable = context.observable();

    final AtomicBoolean result = new AtomicBoolean();

    final AtomicInteger conditionRun = new AtomicInteger();
    final AtomicInteger effectRun = new AtomicInteger();

    final String name = ValueUtil.randomString();
    final SafeFunction<Boolean> condition = () -> {
      conditionRun.incrementAndGet();
      observable.reportObserved();
      return result.get();
    };
    final SafeProcedure procedure = effectRun::incrementAndGet;

    final Watcher
      watcher = new Watcher( context, null, name, true, true, condition, procedure, Flags.PRIORITY_NORMAL, true );

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
    assertEquals( Disposable.isDisposed( watcher ), false );

    result.set( true );
    Disposable.dispose( watcher.getObserver() );

    assertEquals( Disposable.isDisposed( watcher ), true );
    assertEquals( Disposable.isDisposed( watcher.getObserver() ), true );

    context.action( ValueUtil.randomString(), true, observable::reportChanged );

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
  }

  @Test
  public void conditionReadOnlyTransaction()
    throws Throwable
  {
    ignoreObserverErrors();

    final ArezContext context = Arez.context();

    final AtomicInteger errorCount = new AtomicInteger();

    final ArrayList<Observer> observersErrored = new ArrayList<>();

    final ObserverErrorHandler handler = ( observer, error, throwable ) -> {
      errorCount.incrementAndGet();
      assertEquals( ObserverError.REACTION_ERROR, error );
      observersErrored.add( observer );
    };
    context.addObserverErrorHandler( handler );

    final ObservableValue observable = context.observable();

    final AtomicBoolean result = new AtomicBoolean();

    final AtomicInteger conditionRun = new AtomicInteger();
    final AtomicInteger effectRun = new AtomicInteger();

    final String name = ValueUtil.randomString();
    final SafeFunction<Boolean> condition = () -> {
      conditionRun.incrementAndGet();
      observable.reportObserved();
      observable.reportChanged();
      return result.get();
    };
    final SafeProcedure procedure = effectRun::incrementAndGet;

    new Watcher( context, null, name, true, true, condition, procedure, Flags.PRIORITY_NORMAL, true );

    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
    assertEquals( errorCount.get(), 2 );

    //First error is the computed
    assertEquals( context.getSpy().asObserverInfo( observersErrored.get( 0 ) ).isComputedValue(), true );
    //Second error was the autorun that called the computed
    assertEquals( context.getSpy().asObserverInfo( observersErrored.get( 1 ) ).isComputedValue(), false );

    result.set( true );

    // Attempt to run condition. Should produce another error and condition evaluation
    context.action( ValueUtil.randomString(), true, observable::reportChanged );

    assertEquals( conditionRun.get(), 2 );
    assertEquals( effectRun.get(), 0 );
    assertEquals( errorCount.get(), 3 );

    //Next error is the computed again
    assertEquals( context.getSpy().asObserverInfo( observersErrored.get( 0 ) ).isComputedValue(), true );
  }

  @Test
  public void verifyEffectWhenReadOnlyTransaction()
    throws Exception
  {
    ignoreObserverErrors();

    final ArezContext context = Arez.context();

    final AtomicInteger errorCount = new AtomicInteger();

    final ObserverErrorHandler handler = ( observer, error, throwable ) -> errorCount.incrementAndGet();
    context.addObserverErrorHandler( handler );

    final ObservableValue observable = context.observable();

    final AtomicInteger effectRun = new AtomicInteger();

    final SafeProcedure effect = () -> {
      effectRun.incrementAndGet();
      observable.reportObserved();
      observable.reportChanged();
    };

    final SafeFunction<Boolean> function = () -> {
      observeDependency();
      return true;
    };
    new Watcher( context, null, ValueUtil.randomString(), false, true, function, effect, Flags.PRIORITY_NORMAL, true );

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

    final ObservableValue observable = context.observable();

    final AtomicInteger effectRun = new AtomicInteger();

    final SafeProcedure effect = () -> {
      effectRun.incrementAndGet();
      observable.reportObserved();
      observable.reportChanged();
    };

    final SafeFunction<Boolean> function = () -> {
      observeDependency();
      return true;
    };
    new Watcher( context, null, ValueUtil.randomString(), true, true, function, effect, Flags.PRIORITY_NORMAL, true );

    assertEquals( effectRun.get(), 1 );
    assertEquals( errorCount.get(), 0 );
  }

  @Test
  public void verifyEffectWhenNoReadWriteOccursAndAllowed()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final AtomicInteger errorCount = new AtomicInteger();

    context.addObserverErrorHandler( ( observer, error, throwable ) -> errorCount.incrementAndGet() );

    final AtomicInteger effectRun = new AtomicInteger();

    final SafeProcedure effect = effectRun::incrementAndGet;

    final SafeFunction<Boolean> function = () -> {
      observeDependency();
      return true;
    };
    new Watcher( context, null, ValueUtil.randomString(), true, false, function, effect, Flags.PRIORITY_NORMAL, true );

    assertEquals( effectRun.get(), 1 );
    assertEquals( errorCount.get(), 0 );
  }

  @Test
  public void verifyEffectWhenNoReadWriteOccursAndDisallowed()
    throws Exception
  {
    ignoreObserverErrors();

    final ArezContext context = Arez.context();

    final AtomicInteger errorCount = new AtomicInteger();

    context.addObserverErrorHandler( ( observer, error, throwable ) -> errorCount.incrementAndGet() );

    final AtomicInteger effectRun = new AtomicInteger();

    final SafeProcedure effect = effectRun::incrementAndGet;

    final SafeFunction<Boolean> function = () -> {
      observeDependency();
      return true;
    };
    new Watcher( context, null, ValueUtil.randomString(), true, true, function, effect, Flags.PRIORITY_NORMAL, true );

    assertEquals( effectRun.get(), 1 );
    assertEquals( errorCount.get(), 1 );
  }
}
