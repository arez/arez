package org.realityforge.arez;

import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * TODO: This class does not fully test the Observable.
 */
public class ObservableTest
  extends AbstractArezTest
{
  @Test
  public void initialState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final TestObservable observable = new TestObservable( context, name, null );
    assertEquals( observable.getName(), name );
    assertEquals( observable.getContext(), context );
    assertEquals( observable.toString(), name );
    assertEquals( observable.isPendingDeactivation(), false );
    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void addObserver()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    setCurrentTransaction( context, observer );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), null );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.INACTIVE );

    // Handle addition of observer in correct state
    observable.addObserver( observer );

    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( observable.hasObservers(), true );
    assertEquals( observable.hasObserver( observer ), true );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.INACTIVE );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void addObserver_updatesLestStaleObserverState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    setCurrentTransaction( context, observer );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), null );
    observable.setLeastStaleObserverState( ObserverState.STALE );

    observer.setState( ObserverState.POSSIBLY_STALE );

    observable.addObserver( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void addObserver_duplicate()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    setCurrentTransaction( context, observer );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), null );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );

    // Handle addition of observer in correct state
    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( observable.hasObservers(), true );
    assertEquals( observable.hasObserver( observer ), true );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Attempting to add observer named '" + observer.getName() + "' to observable named '" +
                  observable.getName() + "' when observer is already observing observable." );

    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( observable.hasObservers(), true );
    assertEquals( observable.hasObserver( observer ), true );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void addObserver_noActiveTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Attempt to invoke addObserver on observable named '" +
                  observable.getName() + "' when there is no active transaction." );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void setLeastStaleObserverState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), null );
    setCurrentTransaction( context, observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.INACTIVE );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void setLeastStaleObserverState_noActiveTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.INACTIVE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE ) );

    assertEquals( exception.getMessage(),
                  "Attempt to invoke setLeastStaleObserverState on observable named '" +
                  observable.getName() + "' when there is no active transaction." );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.INACTIVE );
  }

  @Test
  public void invariantLeastStaleObserverState_noObservers()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context, new Observer( context, ValueUtil.randomString() ) );
    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    observable.setLeastStaleObserverState( ObserverState.STALE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::invariantLeastStaleObserverState );

    assertEquals( exception.getMessage(),
                  "Calculated leastStaleObserverState on observable named '" +
                  observable.getName() + "' is 'INACTIVE' which is unexpectedly less than cached value 'STALE'." );

    observable.setLeastStaleObserverState( ObserverState.INACTIVE );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void invariantLeastStaleObserverState_multipleObservers()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context, new Observer( context, ValueUtil.randomString() ) );

    final Observer observer1 = new Observer( context, ValueUtil.randomString() );
    final Observer observer2 = new Observer( context, ValueUtil.randomString() );
    final Observer observer3 = new Observer( context, ValueUtil.randomString() );

    observer1.setState( ObserverState.UP_TO_DATE );
    observer2.setState( ObserverState.POSSIBLY_STALE );
    observer3.setState( ObserverState.STALE );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    observer1.getDependencies().add( observable );
    observer2.getDependencies().add( observable );
    observer3.getDependencies().add( observable );

    observable.addObserver( observer1 );
    observable.addObserver( observer2 );
    observable.addObserver( observer3 );

    observable.setLeastStaleObserverState( ObserverState.STALE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::invariantLeastStaleObserverState );

    assertEquals( exception.getMessage(),
                  "Calculated leastStaleObserverState on observable named '" +
                  observable.getName() + "' is 'UP_TO_DATE' which is unexpectedly less than cached value 'STALE'." );

    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observable.invariantLeastStaleObserverState();
  }

  private void setCurrentTransaction( @Nonnull final ArezContext context, @Nonnull final Observer observer )
  {
    context.setTransaction( new Transaction( context,
                                             null,
                                             ValueUtil.randomString(),
                                             observer.getMode(),
                                             observer ) );
  }
}
