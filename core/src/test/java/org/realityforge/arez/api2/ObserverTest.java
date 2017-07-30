package org.realityforge.arez.api2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObserverTest
  extends AbstractArezTest
{
  static class TestObservable
    extends Observable
  {
    TestObservable( @Nonnull final ArezContext context,
                    @Nullable final String name,
                    @Nullable final Derivation derivation )
    {
      super( context, name, derivation );
    }
  }

  @Test
  public void nodeState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final TestObservable observable = new TestObservable( context, name, null );
    assertEquals( observable.getName(), name );
    assertEquals( observable.getContext(), context );
    assertEquals( observable.toString(), name );
  }

  @Test
  public void addObserver()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), null );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );

    final TestObserver observer = new TestObserver();

    // Handle addition of observer in correct state
    observable.addObserver( observer );

    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( observable.hasObservers(), true );
    assertEquals( observable.getObservers().get( 0 ), observer );
  }

  @Test
  public void addObserver_badState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), null );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );

    final TestObserver observer = new TestObserver();

    // Handle addition of observer in incorrect state
    observer.setState( ObserverState.STALE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Attempting to add observer named '" + observer.getName() + "' to observable named '" +
                  observable.getName() + "' when observer is in state 'STALE' rather than the expected 'NOT_TRACKING'." );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );
  }

  @Test
  public void addObserver_duplicate()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), null );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );

    final TestObserver observer = new TestObserver();

    // Handle addition of observer in correct state
    observable.addObserver( observer );

    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( observable.hasObservers(), true );
    assertEquals( observable.getObservers().get( 0 ), observer );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Attempting to add observer named '" + observer.getName() + "' to observable named '" +
                  observable.getName() + "' when observer is already observing observable." );

    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( observable.hasObservers(), true );
    assertEquals( observable.getObservers().get( 0 ), observer );
  }
}
