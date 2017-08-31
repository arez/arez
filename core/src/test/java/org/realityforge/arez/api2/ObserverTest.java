package org.realityforge.arez.api2;

import java.util.ArrayList;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObserverTest
  extends AbstractArezTest
{
  @Test
  public void initialState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final int nextNodeId = context.currentNextNodeId();
    final Observer observer = new Observer( context, name );

    // Verify all "Node" behaviour
    assertEquals( observer.getContext(), context );
    assertEquals( observer.getName(), name );
    assertEquals( observer.getId(), nextNodeId );
    assertEquals( observer.toString(), name );

    // Starts out inactive and inactive means no dependencies
    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.isActive(), false );
    assertEquals( observer.isInactive(), true );
    assertEquals( observer.getDependencies().size(), 0 );

    // All the hooks start out null
    assertEquals( observer.getOnActivate(), null );
    assertEquals( observer.getOnDeactivate(), null );
    assertEquals( observer.getOnStale(), null );

    final Action onActivate = () -> {
    };
    final Action onDeactivate = () -> {
    };
    final Action onStale = () -> {
    };

    // Ensure hooks can be modified
    observer.setOnActivate( onActivate );
    observer.setOnDeactivate( onDeactivate );
    observer.setOnStale( onStale );

    assertEquals( observer.getOnActivate(), onActivate );
    assertEquals( observer.getOnDeactivate(), onDeactivate );
    assertEquals( observer.getOnStale(), onStale );

    observer.invariantState();
  }

  @Test
  public void invariantDependenciesBackLink()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );
    observer.getDependencies().add( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.invariantDependenciesBackLink( "TEST1" ) );

    assertEquals( exception.getMessage(),
                  "TEST1: Observer named '" + observer.getName() + "' has dependency observable named '" +
                  observable.getName() + "' which does not contain the observer in the list of observers." );

    //Setup correct back link
    observable.addObserver( observer );

    // Back link created so should be good
    observer.invariantDependenciesBackLink( "TEST2" );
  }

  @Test
  public void invariantDependenciesUnique()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    observer.getDependencies().add( observable );

    observer.invariantDependenciesUnique( "TEST1" );

    // Add a duplicate
    observer.getDependencies().add( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.invariantDependenciesUnique( "TEST2" ) );

    assertEquals( exception.getMessage(),
                  "TEST2: The set of dependencies in observer named '" + observer.getName() + "' is " +
                  "not unique. Current list: '[" + observable.getName() + ", " + observable.getName() + "]'." );
  }

  @Test
  public void invariantState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );

    observer.invariantState();

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );
    observer.getDependencies().add( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observer::invariantState );

    assertEquals( exception.getMessage(),
                  "Observer named '" + observer.getName() + "' is inactive " +
                  "but still has dependencies: [" + observable.getName() + "]." );
  }

  @Test
  public void replaceDependencies()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    observer.setState( ObserverState.UP_TO_DATE );

    final ArrayList<Observable> originalDependencies = observer.getDependencies();

    assertEquals( originalDependencies.isEmpty(), true );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    final ArrayList<Observable> newDependencies = new ArrayList<>();
    newDependencies.add( observable );
    observable.addObserver( observer );

    observer.replaceDependencies( newDependencies );

    assertEquals( observer.getDependencies().size(), 1 );
    assertTrue( observer.getDependencies() != originalDependencies );
    assertTrue( observer.getDependencies().contains( observable ) );
  }

  @Test
  public void replaceDependencies_duplicateDependency()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    observer.setState( ObserverState.UP_TO_DATE );

    final ArrayList<Observable> originalDependencies = observer.getDependencies();

    assertEquals( originalDependencies.isEmpty(), true );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    final ArrayList<Observable> newDependencies = new ArrayList<>();
    newDependencies.add( observable );
    newDependencies.add( observable );
    observable.addObserver( observer );

    assertThrows( () -> observer.replaceDependencies( newDependencies ) );
  }

  @Test
  public void replaceDependencies_notBacklinedDependency()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    observer.setState( ObserverState.UP_TO_DATE );

    final ArrayList<Observable> originalDependencies = observer.getDependencies();

    assertEquals( originalDependencies.isEmpty(), true );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    final ArrayList<Observable> newDependencies = new ArrayList<>();
    newDependencies.add( observable );

    assertThrows( () -> observer.replaceDependencies( newDependencies ) );
  }

  @Test
  public void clearDependencies()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    observer.setState( ObserverState.UP_TO_DATE );

    final TestObservable observable1 = new TestObservable( context, ValueUtil.randomString() );
    final TestObservable observable2 = new TestObservable( context, ValueUtil.randomString() );

    observer.getDependencies().add( observable1 );
    observer.getDependencies().add( observable2 );
    observable1.addObserver( observer );
    observable2.addObserver( observer );

    assertEquals( observer.getDependencies().size(), 2 );
    assertEquals( observable1.getObservers().size(), 1 );
    assertEquals( observable2.getObservers().size(), 1 );

    observer.clearDependencies();

    assertEquals( observer.getDependencies().size(), 0 );
    assertEquals( observable1.getObservers().size(), 0 );
    assertEquals( observable2.getObservers().size(), 0 );
  }
}
