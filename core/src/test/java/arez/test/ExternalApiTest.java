package arez.test;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.ArezObserverTestUtil;
import arez.ArezTestUtil;
import arez.ComputedValue;
import arez.Disposable;
import arez.ObservableValue;
import arez.Observer;
import arez.ObserverErrorHandler;
import arez.Priority;
import arez.Procedure;
import arez.SafeFunction;
import arez.SpyEventHandler;
import arez.Zone;
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
@SuppressWarnings( "Duplicates" )
public class ExternalApiTest
  extends AbstractArezTest
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
    context.autorun( ValueUtil.randomString(), false, action, false );

    assertEquals( callCount.get(), 0 );

    context.triggerScheduler();

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void areNamesEnabled()
    throws Exception
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
    ArezTestUtil.resetState();
    assertTrue( Arez.areZonesEnabled() );
  }

  @Test
  public void areReferencesEnabled()
  {
    ArezTestUtil.disableReferences();
    assertFalse( Arez.areReferencesEnabled() );
    ArezTestUtil.enableReferences();
    ArezTestUtil.resetState();
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
  public void createComputedValue()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ComputedValue<String> computedValue = context.computed( name, function );

    context.action( ValueUtil.randomString(), true, () -> {
      assertEquals( computedValue.getName(), name );
      assertEquals( computedValue.get(), "" );
      assertEquals( context.isTransactionActive(), true );

      computedValue.dispose();

      assertThrows( computedValue::get );
    } );
  }

  @Test
  public void ComputedValue_reportPossiblyChanged()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger computedCallCount = new AtomicInteger();
    final AtomicInteger autorunCallCount = new AtomicInteger();
    final AtomicInteger result = new AtomicInteger();
    final AtomicReference<String> expected = new AtomicReference<>();

    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      computedCallCount.incrementAndGet();
      return String.valueOf( result.get() );
    };
    final ComputedValue<String> computedValue =
      context.computed( null, name, function, null, null, null, null, Priority.NORMAL, false, false, false, false );

    assertEquals( autorunCallCount.get(), 0 );
    assertEquals( computedCallCount.get(), 0 );

    expected.set( "0" );

    context.autorun( () -> {
      autorunCallCount.incrementAndGet();
      assertEquals( computedValue.get(), expected.get() );
    } );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( computedCallCount.get(), 1 );

    context.safeAction( computedValue::reportPossiblyChanged );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( computedCallCount.get(), 2 );

    result.set( 23 );
    expected.set( "23" );

    assertEquals( computedCallCount.get(), 2 );

    context.safeAction( computedValue::reportPossiblyChanged );

    assertEquals( autorunCallCount.get(), 2 );
    assertEquals( computedCallCount.get(), 3 );
  }

  @Test
  public void createReactionObserver()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final String name = ValueUtil.randomString();
    final Observer observer = context.autorun( name, false, () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, true );

    assertEquals( observer.getName(), name );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );
    assertEquals( callCount.get(), 1 );

    observer.dispose();

    assertEquals( ArezObserverTestUtil.isActive( observer ), false );
  }

  @Test
  public void observerErrorHandler()
    throws Exception
  {
    setIgnoreObserverErrors( true );
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final ObserverErrorHandler handler = ( observer, error, throwable ) -> callCount.incrementAndGet();
    context.addObserverErrorHandler( handler );

    final Procedure reaction = () -> {
      throw new RuntimeException();
    };
    // This will run immediately and generate an exception
    context.autorun( ValueUtil.randomString(), false, reaction, true );

    assertEquals( callCount.get(), 1 );

    context.removeObserverErrorHandler( handler );

    // This will run immediately and generate an exception
    context.autorun( ValueUtil.randomString(), false, reaction, true );

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void spyEventHandler()
    throws Exception
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
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ObservableValue observableValue = context.observable();

    final AtomicInteger reactionCount = new AtomicInteger();

    final Observer observer =
      context.autorun( ValueUtil.randomString(),
                       false,
                       () -> {
                         observableValue.reportObserved();
                         reactionCount.incrementAndGet();
                       },
                       true );

    assertEquals( reactionCount.get(), 1 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );

    context.safeAction( ValueUtil.randomString(), true, observableValue::reportChanged );

    assertEquals( reactionCount.get(), 2 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );
  }

  @Test
  public void interactionWithSingleObservable()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ObservableValue observableValue = context.observable();

    final AtomicInteger reactionCount = new AtomicInteger();

    final Observer observer =
      context.autorun( ValueUtil.randomString(),
                       false,
                       () -> {
                         observableValue.reportObserved();
                         reactionCount.incrementAndGet();
                       },
                       true );

    assertEquals( reactionCount.get(), 1 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );

    // Run an "action"
    context.action( ValueUtil.randomString(), true, observableValue::reportChanged );

    assertEquals( reactionCount.get(), 2 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );
  }

  @Test
  public void interactionWithMultipleObservable()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ObservableValue observableValue1 = context.observable();
    final ObservableValue observableValue2 = context.observable();
    final ObservableValue observableValue3 = context.observable();
    final ObservableValue observableValue4 = context.observable();

    final AtomicInteger reactionCount = new AtomicInteger();

    final Observer observer =
      context.autorun( ValueUtil.randomString(),
                       false,
                       () -> {
                         observableValue1.reportObserved();
                         observableValue2.reportObserved();
                         observableValue3.reportObserved();
                         reactionCount.incrementAndGet();
                       },
                       true );

    assertEquals( reactionCount.get(), 1 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );

    // Run an "action"
    context.action( ValueUtil.randomString(), true, observableValue1::reportChanged );

    assertEquals( reactionCount.get(), 2 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );

    // Update observer1+observer2 in transaction
    context.action( ValueUtil.randomString(),
                    true,
                    () -> {
                      observableValue1.reportChanged();
                      observableValue2.reportChanged();
                    } );

    assertEquals( reactionCount.get(), 3 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );

    context.action( ValueUtil.randomString(),
                    true,
                    () -> {
                      observableValue3.reportChanged();
                      observableValue4.reportChanged();
                    } );

    assertEquals( reactionCount.get(), 4 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );

    // observableValue4 should not cause a reaction as not observed
    context.action( ValueUtil.randomString(), true, observableValue4::reportChanged );

    assertEquals( reactionCount.get(), 4 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );
  }

  @Test
  public void action_function()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ObservableValue observableValue = context.observable();

    assertNotInTransaction( context, observableValue );

    final String expectedValue = ValueUtil.randomString();

    final String v0 =
      context.action( ValueUtil.randomString(), false, () -> {
        assertInTransaction( context, observableValue );
        return expectedValue;
      } );

    assertNotInTransaction( context, observableValue );

    assertEquals( v0, expectedValue );
  }

  @Test
  public void action_safeFunction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ObservableValue observableValue = context.observable();

    assertNotInTransaction( context, observableValue );

    final String expectedValue = ValueUtil.randomString();

    final String v0 =
      context.safeAction( ValueUtil.randomString(), false, () -> {
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
    final ObservableValue observableValue = context.observable();

    assertNotInTransaction( context, observableValue );

    context.action( ValueUtil.randomString(), false, () -> {
      assertInTransaction( context, observableValue );

      //First nested exception
      context.action( ValueUtil.randomString(), false, () -> {
        assertInTransaction( context, observableValue );

        //Second nested exception
        context.action( ValueUtil.randomString(), false, () -> assertInTransaction( context, observableValue ) );

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

    final ObservableValue observableValue = context.observable();

    assertNotInTransaction( context, observableValue );

    final String expectedValue = ValueUtil.randomString();

    final String v0 =
      context.action( ValueUtil.randomString(), false, () -> {
        assertInTransaction( context, observableValue );

        //First nested exception
        final String v1 =
          context.action( ValueUtil.randomString(), false, () -> {
            assertInTransaction( context, observableValue );

            //Second nested exception
            final String v2 =
              context.action( ValueUtil.randomString(), false, () -> {
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
  public void supportsMultipleContexts()
    throws Throwable
  {
    final Zone zone1 = Arez.createZone();
    final Zone zone2 = Arez.createZone();

    final ArezContext context1 = zone1.getContext();
    final ArezContext context2 = zone2.getContext();
    final ObservableValue observableValue1 = context1.observable();
    final ObservableValue observableValue2 = context2.observable();

    final AtomicInteger autorunCallCount1 = new AtomicInteger();
    final AtomicInteger autorunCallCount2 = new AtomicInteger();

    context1.autorun( () -> {
      observableValue1.reportObserved();
      autorunCallCount1.incrementAndGet();
    } );

    context2.autorun( () -> {
      observableValue2.reportObserved();
      autorunCallCount2.incrementAndGet();
    } );

    assertEquals( autorunCallCount1.get(), 1 );
    assertEquals( autorunCallCount2.get(), 1 );

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

          assertEquals( autorunCallCount1.get(), 1 );
          context1.action( () -> assertInTransaction( context1, observableValue1 ) );
          // Still no autorun reaction as it has transaction up the stack
          assertEquals( autorunCallCount1.get(), 1 );

          assertEquals( autorunCallCount2.get(), 1 );
        } );

        // Second context runs now as it got to it's top level transaction
        assertEquals( autorunCallCount2.get(), 2 );

        assertInTransaction( context1, observableValue1 );
        assertNotInTransaction( context2, observableValue2 );
      } );

      assertInTransaction( context1, observableValue1 );
      assertNotInTransaction( context2, observableValue2 );
    } );

    assertEquals( autorunCallCount1.get(), 2 );
    assertEquals( autorunCallCount2.get(), 2 );

    assertNotInTransaction( context1, observableValue1 );
    assertNotInTransaction( context2, observableValue2 );
  }

  @Test
  public void pauseScheduler()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Disposable lock1 = context.pauseScheduler();
    assertEquals( context.isSchedulerPaused(), true );

    final AtomicInteger callCount = new AtomicInteger();

    // This would normally be scheduled and run now but scheduler should be paused
    context.autorun( ValueUtil.randomString(), false, () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, false );
    context.triggerScheduler();

    assertEquals( callCount.get(), 0 );

    final Disposable lock2 = context.pauseScheduler();

    lock2.dispose();

    assertEquals( context.isSchedulerPaused(), true );

    // Already disposed so this is a noop
    lock2.dispose();

    assertEquals( callCount.get(), 0 );

    lock1.dispose();

    assertEquals( callCount.get(), 1 );
    assertEquals( context.isSchedulerPaused(), false );
  }

  /**
   * Test we are in a transaction by trying to observe an observableValue.
   */
  private void assertInTransaction( @Nonnull final ArezContext context, @Nonnull final ObservableValue observableValue )
  {
    assertEquals( context.isTransactionActive(), true );
    observableValue.reportObserved();
  }

  /**
   * Test we are not in a transaction by trying to observe an observableValue.
   */
  private void assertNotInTransaction( @Nonnull final ArezContext context,
                                       @Nonnull final ObservableValue observableValue )
  {
    assertEquals( context.isTransactionActive(), false );
    assertThrows( observableValue::reportObserved );
  }
}
