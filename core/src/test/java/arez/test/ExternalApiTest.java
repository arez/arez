package arez.test;

import arez.AbstractTest;
import arez.ActionFlags;
import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.ComputableValue;
import arez.ObservableValue;
import arez.Observer;
import arez.ObserverErrorHandler;
import arez.Procedure;
import arez.SafeFunction;
import arez.SchedulerLock;
import arez.Zone;
import arez.spy.SpyEventHandler;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * This class tests all the public API of Arez and identifies all
 * the elements that should be visible outside package.
 */
public final class ExternalApiTest
  extends AbstractTest
{
  @Test
  public void triggerScheduler()
  {
    final ArezContext context = Arez.context();
    final AtomicInteger callCount = new AtomicInteger();

    final Procedure action = () -> {
      observeADependency();
      callCount.incrementAndGet();
    };
    context.observer( action, Observer.Flags.RUN_LATER );

    assertEquals( callCount.get(), 0 );

    context.triggerScheduler();

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void areNamesEnabled()
  {
    assertTrue( Arez.areNamesEnabled() );
    ArezTestUtil.disableNames();
    assertFalse( Arez.areNamesEnabled() );
  }

  @Test
  public void arePropertyIntrospectorsEnabled()
  {
    assertTrue( Arez.arePropertyIntrospectorsEnabled() );
    ArezTestUtil.disablePropertyIntrospectors();
    assertFalse( Arez.arePropertyIntrospectorsEnabled() );
  }

  @Test
  public void areRepositoryResultsUnmodifiable()
  {
    assertTrue( Arez.areCollectionsPropertiesUnmodifiable() );
    ArezTestUtil.makeCollectionPropertiesModifiable();
    assertFalse( Arez.areCollectionsPropertiesUnmodifiable() );
  }

  @Test
  public void areSpiesEnabled()
  {
    assertTrue( Arez.areSpiesEnabled() );
    ArezTestUtil.disableSpies();
    assertFalse( Arez.areSpiesEnabled() );
  }

  @Test
  public void areZonesEnabled()
  {
    ArezTestUtil.disableZones();
    assertFalse( Arez.areZonesEnabled() );
    ArezTestUtil.enableZones();
    assertTrue( Arez.areZonesEnabled() );
  }

  @Test
  public void areReferencesEnabled()
  {
    ArezTestUtil.disableReferences();
    assertFalse( Arez.areReferencesEnabled() );
    ArezTestUtil.enableReferences();
    assertTrue( Arez.areReferencesEnabled() );
  }

  @Test
  public void isVerifyEnabled()
  {
    ArezTestUtil.disableVerify();
    assertFalse( Arez.isVerifyEnabled() );
    ArezTestUtil.enableVerify();
    assertTrue( Arez.isVerifyEnabled() );
  }

  @Test
  public void areNativeComponentsEnabled()
  {
    assertTrue( Arez.areNativeComponentsEnabled() );
    ArezTestUtil.disableNativeComponents();
    assertFalse( Arez.areNativeComponentsEnabled() );
  }

  @Test
  public void areRegistriesEnabled()
  {
    assertTrue( Arez.areRegistriesEnabled() );
    ArezTestUtil.disableRegistries();
    assertFalse( Arez.areRegistriesEnabled() );
  }

  @Test
  public void purgeTasksWhenRunawayDetected()
  {
    ArezTestUtil.noPurgeTasksWhenRunawayDetected();
    assertFalse( Arez.purgeTasksWhenRunawayDetected() );
    ArezTestUtil.purgeTasksWhenRunawayDetected();
    assertTrue( Arez.purgeTasksWhenRunawayDetected() );
  }

  @Test
  public void isSchedulerActive_insideTask()
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isSchedulerActive() );
    context.task( () -> assertTrue( context.isSchedulerActive() ) );
    assertFalse( context.isSchedulerActive() );
  }

  @Test
  public void isSchedulerActive_insideObserver()
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isSchedulerActive() );
    context.observer( () -> assertTrue( context.isSchedulerActive() ), Observer.Flags.AREZ_OR_NO_DEPENDENCIES );
    assertFalse( context.isSchedulerActive() );
  }

  @Test
  public void createComputableValue()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ComputableValue<String> computableValue = context.computable( name, function );

    assertEquals( computableValue.getContext(), context );

    context.action( () -> {
      assertEquals( computableValue.getName(), name );
      assertEquals( computableValue.get(), "" );
      assertTrue( context.isTransactionActive() );
      assertTrue( context.isReadWriteTransactionActive() );
      assertFalse( context.isTrackingTransactionActive() );
      assertFalse( context.isComputableTransactionActive() );

      computableValue.dispose();

      assertThrows( computableValue::get );
    } );
  }

  @Test
  public void ComputableValue_reportPossiblyChanged()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger computedCallCount = new AtomicInteger();
    final AtomicInteger autorunCallCount = new AtomicInteger();
    final AtomicInteger result = new AtomicInteger();
    final AtomicReference<String> expected = new AtomicReference<>();

    final SafeFunction<String> function = () -> {
      observeADependency();
      computedCallCount.incrementAndGet();
      return String.valueOf( result.get() );
    };
    final ComputableValue<String> computableValue =
      context.computable( function, ComputableValue.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    assertEquals( autorunCallCount.get(), 0 );
    assertEquals( computedCallCount.get(), 0 );

    expected.set( "0" );

    context.observer( () -> {
      autorunCallCount.incrementAndGet();
      assertEquals( computableValue.get(), expected.get() );

      assertTrue( context.isTransactionActive() );
      assertTrue( context.isReadOnlyTransactionActive() );
      assertFalse( context.isReadWriteTransactionActive() );
      assertTrue( context.isTrackingTransactionActive() );
      assertTrue( context.isComputableTransactionActive() );
    } );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( computedCallCount.get(), 1 );

    context.safeAction( computableValue::reportPossiblyChanged );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( computedCallCount.get(), 2 );

    result.set( 23 );
    expected.set( "23" );

    assertEquals( computedCallCount.get(), 2 );

    context.safeAction( computableValue::reportPossiblyChanged );

    assertEquals( autorunCallCount.get(), 2 );
    assertEquals( computedCallCount.get(), 3 );
  }

  @Test
  public void observerErrorHandler()
  {
    ignoreObserverErrors();
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final ObserverErrorHandler handler = ( observer, error, throwable ) -> callCount.incrementAndGet();
    context.addObserverErrorHandler( handler );

    final Procedure reaction = () -> {
      throw new RuntimeException();
    };
    // This will run immediately and generate an exception
    context.observer( reaction );

    assertEquals( callCount.get(), 1 );

    context.removeObserverErrorHandler( handler );

    // This will run immediately and generate an exception
    context.observer( reaction );

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void spyEventHandler()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final SpyEventHandler handler = e -> callCount.incrementAndGet();
    context.getSpy().addSpyEventHandler( handler );

    // Generate an event
    context.observable();

    assertEquals( callCount.get(), 1 );

    context.getSpy().removeSpyEventHandler( handler );

    // Generate an event
    context.observable();

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void safeProcedure_interactionWithSingleObservable()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable();
    assertEquals( observableValue.getContext(), context );

    final AtomicInteger reactionCount = new AtomicInteger();

    final Observer observer =
      context.observer( () -> {
        observableValue.reportObserved();
        reactionCount.incrementAndGet();
        assertTrue( context.isTransactionActive() );
        assertFalse( context.isReadWriteTransactionActive() );
        assertTrue( context.isTrackingTransactionActive() );
        assertFalse( context.isComputableTransactionActive() );
      } );

    assertEquals( reactionCount.get(), 1 );
    assertTrue( context.getSpy().asObserverInfo( observer ).isActive() );

    context.safeAction( () -> {
      observableValue.reportChanged();
      assertTrue( context.isTransactionActive() );
      assertTrue( context.isReadWriteTransactionActive() );
      assertFalse( context.isTrackingTransactionActive() );
      assertFalse( context.isComputableTransactionActive() );
    } );

    assertEquals( reactionCount.get(), 2 );
    assertTrue( context.getSpy().asObserverInfo( observer ).isActive() );
  }

  @Test
  public void interactionWithSingleObservable()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable();

    final AtomicInteger reactionCount = new AtomicInteger();

    final Observer observer =
      context.observer( () -> {
        observableValue.reportObserved();
        reactionCount.incrementAndGet();
      } );

    assertEquals( reactionCount.get(), 1 );
    assertTrue( context.getSpy().asObserverInfo( observer ).isActive() );

    // Run an "action"
    context.action( observableValue::reportChanged );

    assertEquals( reactionCount.get(), 2 );
    assertTrue( context.getSpy().asObserverInfo( observer ).isActive() );
  }

  @Test
  public void interactionWithMultipleObservable()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue1 = context.observable();
    final ObservableValue<?> observableValue2 = context.observable();
    final ObservableValue<?> observableValue3 = context.observable();
    final ObservableValue<?> observableValue4 = context.observable();

    final AtomicInteger reactionCount = new AtomicInteger();

    final Observer observer =
      context.observer( () -> {
        observableValue1.reportObserved();
        observableValue2.reportObserved();
        observableValue3.reportObserved();
        reactionCount.incrementAndGet();
        assertTrue( context.isTransactionActive() );
        assertFalse( context.isReadWriteTransactionActive() );
        assertTrue( context.isTrackingTransactionActive() );
        assertFalse( context.isComputableTransactionActive() );
      } );

    assertEquals( reactionCount.get(), 1 );
    assertTrue( context.getSpy().asObserverInfo( observer ).isActive() );

    // Run an "action"
    context.action( observableValue1::reportChanged );

    assertEquals( reactionCount.get(), 2 );
    assertTrue( context.getSpy().asObserverInfo( observer ).isActive() );

    // Update observer1+observer2 in transaction
    context.action( () -> {
      observableValue1.reportChanged();
      observableValue2.reportChanged();
    } );

    assertEquals( reactionCount.get(), 3 );
    assertTrue( context.getSpy().asObserverInfo( observer ).isActive() );

    context.action( () -> {
      observableValue3.reportChanged();
      observableValue4.reportChanged();
    } );

    assertEquals( reactionCount.get(), 4 );
    assertTrue( context.getSpy().asObserverInfo( observer ).isActive() );

    // observableValue4 should not cause a reaction as not observed
    context.action( observableValue4::reportChanged );

    assertEquals( reactionCount.get(), 4 );
    assertTrue( context.getSpy().asObserverInfo( observer ).isActive() );
  }

  @Test
  public void action_function()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable();

    assertNotInTransaction( context, observableValue );

    final String expectedValue = ValueUtil.randomString();

    final String v0 =
      context.action( () -> {
        assertInTransaction( context, observableValue );
        return expectedValue;
      } );

    assertNotInTransaction( context, observableValue );

    assertEquals( v0, expectedValue );
  }

  @Test
  public void action_safeFunction()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable();

    assertNotInTransaction( context, observableValue );

    final String expectedValue = ValueUtil.randomString();

    final String v0 =
      context.safeAction( () -> {
        assertInTransaction( context, observableValue );
        return expectedValue;
      } );

    assertNotInTransaction( context, observableValue );

    assertEquals( v0, expectedValue );
  }

  @Test
  public void proceduresCanBeNested()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final ObservableValue<?> observableValue = context.observable();

    assertNotInTransaction( context, observableValue );

    context.action( () -> {
      assertInTransaction( context, observableValue );

      //First nested exception
      context.action( () -> {
        assertInTransaction( context, observableValue );

        //Second nested exception
        context.action( () -> assertInTransaction( context, observableValue ) );

        assertInTransaction( context, observableValue );
      } );

      assertInTransaction( context, observableValue );
    } );

    assertNotInTransaction( context, observableValue );
  }

  @Test
  public void action_nestedFunctions()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable();

    assertNotInTransaction( context, observableValue );

    final String expectedValue = ValueUtil.randomString();

    final String v0 =
      context.action( () -> {
        assertInTransaction( context, observableValue );

        //First nested exception
        final String v1 =
          context.action( () -> {
            assertInTransaction( context, observableValue );

            //Second nested exception
            final String v2 =
              context.action( () -> {
                assertInTransaction( context, observableValue );
                return expectedValue;
              } );

            assertInTransaction( context, observableValue );

            return v2;
          } );

        assertInTransaction( context, observableValue );
        return v1;
      } );

    assertNotInTransaction( context, observableValue );

    assertEquals( v0, expectedValue );
  }

  @Test
  public void contextReturnsZonedContextWithinActions()
  {
    final ArezContext context = Arez.context();

    final Zone zone = Arez.createZone();

    assertEquals( context, Arez.context() );
    zone.safeRun( () -> {
      assertNotEquals( context, Arez.context() );
      assertEquals( zone.getContext(), Arez.context() );
      context.safeAction( () -> {
        assertEquals( context, Arez.context() );
        assertNotEquals( zone.getContext(), Arez.context() );
      }, ActionFlags.NO_VERIFY_ACTION_REQUIRED );
    } );
    assertEquals( context, Arez.context() );
  }

  @Test
  public void supportsMultipleContexts()
    throws Throwable
  {
    final Zone zone1 = Arez.createZone();
    final Zone zone2 = Arez.createZone();

    final ArezContext context1 = zone1.getContext();
    final ArezContext context2 = zone2.getContext();
    final ObservableValue<?> observableValue1 = context1.observable();
    final ObservableValue<?> observableValue2 = context2.observable();

    final AtomicInteger observerCallCount1 = new AtomicInteger();
    final AtomicInteger observerCallCount2 = new AtomicInteger();

    context1.observer( () -> {
      observableValue1.reportObserved();
      observerCallCount1.incrementAndGet();
    } );

    context2.observer( () -> {
      observableValue2.reportObserved();
      observerCallCount2.incrementAndGet();
    } );

    assertEquals( observerCallCount1.get(), 1 );
    assertEquals( observerCallCount2.get(), 1 );

    assertNotInTransaction( context1, observableValue1 );
    assertNotInTransaction( context2, observableValue2 );

    context1.action( () -> {
      assertInTransaction( context1, observableValue1 );

      //First nested exception
      context1.action( () -> {
        assertInTransaction( context1, observableValue1 );
        observableValue1.reportChanged();

        //Second nested exception
        context1.action( () -> assertInTransaction( context1, observableValue1 ) );

        context2.action( () -> {
          assertNotInTransaction( context1, observableValue1 );
          assertInTransaction( context2, observableValue2 );
          observableValue2.reportChanged();

          context2.action( () -> {
            assertNotInTransaction( context1, observableValue1 );
            assertInTransaction( context2, observableValue2 );
            observableValue2.reportChanged();
          } );

          assertEquals( observerCallCount1.get(), 1 );
          context1.action( () -> assertInTransaction( context1, observableValue1 ) );
          // Still no observer reaction as the top-level transaction has not yet completed
          assertEquals( observerCallCount1.get(), 1 );

          assertEquals( observerCallCount2.get(), 1 );
        } );

        // Second context runs now as it got to it's top level transaction
        assertEquals( observerCallCount2.get(), 2 );

        assertInTransaction( context1, observableValue1 );
        assertNotInTransaction( context2, observableValue2 );
      } );

      assertInTransaction( context1, observableValue1 );
      assertNotInTransaction( context2, observableValue2 );
    } );

    assertEquals( observerCallCount1.get(), 2 );
    assertEquals( observerCallCount2.get(), 2 );

    assertNotInTransaction( context1, observableValue1 );
    assertNotInTransaction( context2, observableValue2 );
  }

  @Test
  public void pauseScheduler()
  {
    final ArezContext context = Arez.context();

    final SchedulerLock lock1 = context.pauseScheduler();
    assertTrue( context.isSchedulerPaused() );

    final AtomicInteger callCount = new AtomicInteger();

    // This would normally be scheduled and run now but scheduler should be paused
    context.observer( () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, Observer.Flags.RUN_LATER );
    context.triggerScheduler();

    assertEquals( callCount.get(), 0 );

    final SchedulerLock lock2 = context.pauseScheduler();

    lock2.dispose();

    assertTrue( context.isSchedulerPaused() );

    // Already disposed so this is a noop
    lock2.dispose();

    assertEquals( callCount.get(), 0 );

    lock1.dispose();

    assertEquals( callCount.get(), 1 );
    assertFalse( context.isSchedulerPaused() );
  }

  /**
   * Test we are in a transaction by trying to observe an observableValue.
   */
  private void assertInTransaction( @Nonnull final ArezContext context,
                                    @Nonnull final ObservableValue<?> observableValue )
  {
    assertTrue( context.isTransactionActive() );
    observableValue.reportObserved();
  }

  /**
   * Test we are not in a transaction by trying to observe an observableValue.
   */
  private void assertNotInTransaction( @Nonnull final ArezContext context,
                                       @Nonnull final ObservableValue<?> observableValue )
  {
    assertFalse( context.isTransactionActive() );
    assertThrows( observableValue::reportObserved );
  }
}
