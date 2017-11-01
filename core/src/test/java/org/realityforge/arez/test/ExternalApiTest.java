package org.realityforge.arez.test;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.realityforge.arez.AbstractArezTest;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ArezObserverTestUtil;
import org.realityforge.arez.ArezTestUtil;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Observable;
import org.realityforge.arez.Observer;
import org.realityforge.arez.ObserverErrorHandler;
import org.realityforge.arez.Procedure;
import org.realityforge.arez.SpyEventHandler;
import org.realityforge.arez.Zone;
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

    context.autorun( ValueUtil.randomString(), false, callCount::incrementAndGet, false );

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
    assertFalse( Arez.areRepositoryResultsModifiable() );
    ArezTestUtil.makeRepositoryResultsModifiable();
    assertTrue( Arez.areRepositoryResultsModifiable() );
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
  public void createComputedValue()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final ComputedValue<String> computedValue = context.createComputedValue( name, () -> "", Objects::equals );

    context.action( ValueUtil.randomString(), true, () -> {
      assertEquals( computedValue.getName(), name );
      assertEquals( computedValue.get(), "" );

      computedValue.dispose();

      assertThrows( computedValue::get );
    } );
  }

  @Test
  public void createReactionObserver()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final String name = ValueUtil.randomString();
    final Observer observer = context.autorun( name, false, callCount::incrementAndGet, true );

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
    context.createObservable( ValueUtil.randomString() );

    assertEquals( callCount.get(), 1 );

    context.getSpy().removeSpyEventHandler( handler );

    // Generate an event
    context.createObservable( ValueUtil.randomString() );

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void safeProcedure_interactionWithSingleObservable()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observable observable = context.createObservable( ValueUtil.randomString() );

    final AtomicInteger reactionCount = new AtomicInteger();

    final Observer observer =
      context.autorun( ValueUtil.randomString(),
                       false,
                       () -> {
                         observable.reportObserved();
                         reactionCount.incrementAndGet();
                       },
                       true );

    assertEquals( reactionCount.get(), 1 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );

    context.safeAction( ValueUtil.randomString(), true, observable::reportChanged );

    assertEquals( reactionCount.get(), 2 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );
  }

  @Test
  public void interactionWithSingleObservable()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Observable observable = context.createObservable( ValueUtil.randomString() );

    final AtomicInteger reactionCount = new AtomicInteger();

    final Observer observer =
      context.autorun( ValueUtil.randomString(),
                       false,
                       () -> {
                         observable.reportObserved();
                         reactionCount.incrementAndGet();
                       },
                       true );

    assertEquals( reactionCount.get(), 1 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );

    // Run an "action"
    context.action( ValueUtil.randomString(), true, observable::reportChanged );

    assertEquals( reactionCount.get(), 2 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );
  }

  @Test
  public void interactionWithMultipleObservable()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Observable observable1 = context.createObservable( ValueUtil.randomString() );
    final Observable observable2 = context.createObservable( ValueUtil.randomString() );
    final Observable observable3 = context.createObservable( ValueUtil.randomString() );
    final Observable observable4 = context.createObservable( ValueUtil.randomString() );

    final AtomicInteger reactionCount = new AtomicInteger();

    final Observer observer =
      context.autorun( ValueUtil.randomString(),
                       false,
                       () -> {
                         observable1.reportObserved();
                         observable2.reportObserved();
                         observable3.reportObserved();
                         reactionCount.incrementAndGet();
                       },
                       true );

    assertEquals( reactionCount.get(), 1 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );

    // Run an "action"
    context.action( ValueUtil.randomString(), true, observable1::reportChanged );

    assertEquals( reactionCount.get(), 2 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );

    // Update observer1+observer2 in transaction
    context.action( ValueUtil.randomString(),
                    true,
                    () -> {
                      observable1.reportChanged();
                      observable2.reportChanged();
                    } );

    assertEquals( reactionCount.get(), 3 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );

    context.action( ValueUtil.randomString(),
                    true,
                    () -> {
                      observable3.reportChanged();
                      observable4.reportChanged();
                    } );

    assertEquals( reactionCount.get(), 4 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );

    // observable4 should not cause a reaction as not observed
    context.action( ValueUtil.randomString(), true, observable4::reportChanged );

    assertEquals( reactionCount.get(), 4 );
    assertEquals( ArezObserverTestUtil.isActive( observer ), true );
  }

  @Test
  public void action_function()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Observable observable = context.createObservable( ValueUtil.randomString() );

    assertNotInTransaction( observable );

    final String expectedValue = ValueUtil.randomString();

    final String v0 =
      context.action( ValueUtil.randomString(), false, () -> {
        assertInTransaction( observable );
        return expectedValue;
      } );

    assertNotInTransaction( observable );

    assertEquals( v0, expectedValue );
  }

  @Test
  public void action_safeFunction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observable observable = context.createObservable( ValueUtil.randomString() );

    assertNotInTransaction( observable );

    final String expectedValue = ValueUtil.randomString();

    final String v0 =
      context.safeAction( ValueUtil.randomString(), false, () -> {
        assertInTransaction( observable );
        return expectedValue;
      } );

    assertNotInTransaction( observable );

    assertEquals( v0, expectedValue );
  }

  @Test
  public void proceduresCanBeNested()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final Observable observable = context.createObservable( ValueUtil.randomString() );

    assertNotInTransaction( observable );

    context.action( ValueUtil.randomString(), false, () -> {
      assertInTransaction( observable );

      //First nested exception
      context.action( ValueUtil.randomString(), false, () -> {
        assertInTransaction( observable );

        //Second nested exception
        context.action( ValueUtil.randomString(), false, () -> assertInTransaction( observable ) );

        assertInTransaction( observable );
      } );

      assertInTransaction( observable );
    } );

    assertNotInTransaction( observable );
  }

  @Test
  public void action_nestedFunctions()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Observable observable = context.createObservable( ValueUtil.randomString() );

    assertNotInTransaction( observable );

    final String expectedValue = ValueUtil.randomString();

    final String v0 =
      context.action( ValueUtil.randomString(), false, () -> {
        assertInTransaction( observable );

        //First nested exception
        final String v1 =
          context.action( ValueUtil.randomString(), false, () -> {
            assertInTransaction( observable );

            //Second nested exception
            final String v2 =
              context.action( ValueUtil.randomString(), false, () -> {
                assertInTransaction( observable );
                return expectedValue;
              } );

            assertInTransaction( observable );

            return v2;
          } );

        assertInTransaction( observable );
        return v1;
      } );

    assertNotInTransaction( observable );

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
    final Observable observable1 = context1.createObservable( ValueUtil.randomString() );
    final Observable observable2 = context2.createObservable( ValueUtil.randomString() );

    final AtomicInteger autorunCallCount1 = new AtomicInteger();
    final AtomicInteger autorunCallCount2 = new AtomicInteger();

    context1.autorun( () -> {
      observable1.reportObserved();
      autorunCallCount1.incrementAndGet();
    } );

    context2.autorun( () -> {
      observable2.reportObserved();
      autorunCallCount2.incrementAndGet();
    } );

    assertEquals( autorunCallCount1.get(), 1 );
    assertEquals( autorunCallCount2.get(), 1 );

    assertNotInTransaction( observable1 );
    assertNotInTransaction( observable2 );

    context1.action( () -> {
      assertInTransaction( observable1 );

      //First nested exception
      context1.action( () -> {
        assertInTransaction( observable1 );
        observable1.reportChanged();

        //Second nested exception
        context1.action( () -> assertInTransaction( observable1 ) );

        context2.action( () -> {
          assertNotInTransaction( observable1 );
          assertInTransaction( observable2 );
          observable2.reportChanged();

          context2.action( () -> {
            assertNotInTransaction( observable1 );
            assertInTransaction( observable2 );
            observable2.reportChanged();
          } );

          assertEquals( autorunCallCount1.get(), 1 );
          context1.action( () -> assertInTransaction( observable1 ) );
          // Still no autorun reaction as it has transaction up the stack
          assertEquals( autorunCallCount1.get(), 1 );

          assertEquals( autorunCallCount2.get(), 1 );
        } );

        // Second context runs now as it got to it's top level trnsaction
        assertEquals( autorunCallCount2.get(), 2 );

        assertInTransaction( observable1 );
        assertNotInTransaction( observable2 );
      } );

      assertInTransaction( observable1 );
      assertNotInTransaction( observable2 );
    } );

    assertEquals( autorunCallCount1.get(), 2 );
    assertEquals( autorunCallCount2.get(), 2 );

    assertNotInTransaction( observable1 );
    assertNotInTransaction( observable2 );
  }

  /**
   * Test we are in a transaction by trying to observe an observable.
   */
  private void assertInTransaction( @Nonnull final Observable observable )
  {
    observable.reportObserved();
  }

  /**
   * Test we are not in a transaction by trying to observe an observable.
   */
  private void assertNotInTransaction( @Nonnull final Observable observable )
  {
    assertThrows( observable::reportObserved );
  }
}
